package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.RiskLevel
import com.example.data.model.StationWithLatestTelemetry
import com.example.data.model.TelemetryReadingEntity
import com.example.data.model.ThresholdConfig
import com.example.data.repository.SlopeWatchRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class SlopeWatchViewModel(
    private val repository: SlopeWatchRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    private val _selectedStateFilter = MutableStateFlow<String?>(null)
    private val _selectedRiskFilter = MutableStateFlow<RiskLevel?>(null)
    private val _onlyBookmarked = MutableStateFlow(false)
    private val _selectedStationId = MutableStateFlow<String?>(null)
    private val _isSimulationActive = MutableStateFlow(false)
    private val _statusMessage = MutableStateFlow<String?>(null)

    private var simulationJob: Job? = null

    init {
        viewModelScope.launch {
            repository.initializeSeedDataIfEmpty()
        }
    }

    private val selectedStationHistoryFlow = _selectedStationId.flatMapLatest { id ->
        if (id != null) {
            repository.getStationTelemetryHistory(id)
        } else {
            flowOf(emptyList<TelemetryReadingEntity>())
        }
    }

    val uiState: StateFlow<SlopeWatchUiState> = combine(
        repository.getStationsWithTelemetry(),
        repository.getThresholdConfig(),
        _searchQuery,
        _selectedStateFilter,
        _selectedRiskFilter,
        _onlyBookmarked,
        _selectedStationId,
        selectedStationHistoryFlow,
        _isSimulationActive,
        _statusMessage
    ) { params ->
        val stations = params[0] as List<StationWithLatestTelemetry>
        val thresholds = params[1] as ThresholdConfig
        val query = params[2] as String
        val stateFilter = params[3] as String?
        val riskFilter = params[4] as RiskLevel?
        val bookmarkedOnly = params[5] as Boolean
        val stationId = params[6] as String?
        val history = params[7] as List<TelemetryReadingEntity>
        val isSimActive = params[8] as Boolean
        val statusMsg = params[9] as String?

        val states = stations.map { it.station.state }.distinct().sorted()

        val filtered = stations.filter { item ->
            val matchesQuery = query.isBlank() ||
                item.station.name.contains(query, ignoreCase = true) ||
                item.station.code.contains(query, ignoreCase = true) ||
                item.station.district.contains(query, ignoreCase = true) ||
                item.station.state.contains(query, ignoreCase = true)

            val matchesState = stateFilter == null || item.station.state.equals(stateFilter, ignoreCase = true)
            val matchesRisk = riskFilter == null || item.evaluatedRiskLevel == riskFilter
            val matchesBookmark = !bookmarkedOnly || item.station.isBookmarked

            matchesQuery && matchesState && matchesRisk && matchesBookmark
        }

        val detail = stations.find { it.station.id == stationId }

        SlopeWatchUiState(
            isLoading = false,
            allStations = stations,
            filteredStations = filtered,
            availableStates = states,
            selectedStateFilter = stateFilter,
            selectedRiskFilter = riskFilter,
            searchQuery = query,
            onlyBookmarked = bookmarkedOnly,
            selectedStationId = stationId,
            selectedStationDetail = detail,
            selectedStationHistory = history,
            thresholdConfig = thresholds,
            isSimulationActive = isSimActive,
            statusMessage = statusMsg
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = SlopeWatchUiState(isLoading = true)
    )

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun onSelectStateFilter(state: String?) {
        _selectedStateFilter.value = state
    }

    fun onSelectRiskFilter(risk: RiskLevel?) {
        _selectedRiskFilter.value = risk
    }

    fun onToggleBookmarkFilter() {
        _onlyBookmarked.value = !_onlyBookmarked.value
    }

    fun onSelectStation(stationId: String?) {
        _selectedStationId.value = stationId
    }

    fun toggleBookmark(stationId: String, currentVal: Boolean) {
        viewModelScope.launch {
            repository.toggleBookmark(stationId, currentVal)
        }
    }

    fun saveThresholds(config: ThresholdConfig) {
        viewModelScope.launch {
            repository.saveThresholdConfig(config)
            _statusMessage.value = "Alert thresholds updated successfully"
            delay(3000)
            _statusMessage.value = null
        }
    }

    fun triggerSimulationPulse() {
        viewModelScope.launch {
            _statusMessage.value = "Fetching live telemetry sensor packet..."
            repository.simulateTelemetryPulse()
            delay(1000)
            _statusMessage.value = "Sensor telemetry updated"
            delay(2000)
            _statusMessage.value = null
        }
    }

    fun toggleAutoSimulation(enable: Boolean) {
        _isSimulationActive.value = enable
        if (enable) {
            simulationJob?.cancel()
            simulationJob = viewModelScope.launch {
                while (isActive) {
                    delay(4000)
                    repository.simulateTelemetryPulse()
                }
            }
        } else {
            simulationJob?.cancel()
            simulationJob = null
        }
    }

    fun resetToBaseline() {
        viewModelScope.launch {
            repository.resetToDefaultSeed()
            _statusMessage.value = "All stations reset to geological baseline"
            delay(2500)
            _statusMessage.value = null
        }
    }

    override fun onCleared() {
        super.onCleared()
        simulationJob?.cancel()
    }
}
