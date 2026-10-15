package com.example.ui.viewmodel

import com.example.data.model.RiskLevel
import com.example.data.model.StationWithLatestTelemetry
import com.example.data.model.TelemetryReadingEntity
import com.example.data.model.ThresholdConfig

data class SlopeWatchUiState(
    val isLoading: Boolean = true,
    val allStations: List<StationWithLatestTelemetry> = emptyList(),
    val filteredStations: List<StationWithLatestTelemetry> = emptyList(),
    val availableStates: List<String> = emptyList(),
    val selectedStateFilter: String? = null,
    val selectedRiskFilter: RiskLevel? = null,
    val searchQuery: String = "",
    val onlyBookmarked: Boolean = false,
    val selectedStationId: String? = null,
    val selectedStationDetail: StationWithLatestTelemetry? = null,
    val selectedStationHistory: List<TelemetryReadingEntity> = emptyList(),
    val thresholdConfig: ThresholdConfig = ThresholdConfig(),
    val isSimulationActive: Boolean = false,
    val statusMessage: String? = null
) {
    val criticalCount: Int get() = allStations.count { it.evaluatedRiskLevel == RiskLevel.CRITICAL }
    val highCount: Int get() = allStations.count { it.evaluatedRiskLevel == RiskLevel.HIGH }
    val moderateCount: Int get() = allStations.count { it.evaluatedRiskLevel == RiskLevel.MODERATE }
    val lowCount: Int get() = allStations.count { it.evaluatedRiskLevel == RiskLevel.LOW }
    val totalPopulationAtRisk: Int get() = allStations.sumOf { it.station.populationAtRisk }
}
