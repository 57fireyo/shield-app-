package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.RiskLevel
import com.example.ui.components.StationCard
import com.example.ui.theme.AlertAmber
import com.example.ui.theme.AlertAmberContainer
import com.example.ui.theme.AlertAmberText
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.GreeneryContainer
import com.example.ui.theme.GreeneryContainerText
import com.example.ui.theme.GreeneryPrimary
import com.example.ui.theme.ObsidianBlack
import com.example.ui.theme.PureWhite
import com.example.ui.theme.RedAlertBright
import com.example.ui.theme.RedAlertContainer
import com.example.ui.theme.RedAlertPrimary
import com.example.ui.theme.SurfaceClean
import com.example.ui.viewmodel.SlopeWatchUiState

@Composable
fun DashboardScreen(
    uiState: SlopeWatchUiState,
    onSearchChange: (String) -> Unit,
    onSelectStateFilter: (String?) -> Unit,
    onSelectRiskFilter: (RiskLevel?) -> Unit,
    onToggleBookmarkFilter: () -> Unit,
    onStationClick: (String) -> Unit,
    onBookmarkToggle: (String, Boolean) -> Unit,
    onSimulatePulse: () -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(PureWhite)
    ) {
        // Quick Telemetry Summary Bar
        RegionalSummaryRibbon(
            criticalCount = uiState.criticalCount,
            highCount = uiState.highCount,
            moderateCount = uiState.moderateCount,
            totalCount = uiState.allStations.size,
            isSimulating = uiState.isSimulationActive,
            onRefreshPulse = onSimulatePulse
        )

        // Search Input
        OutlinedTextField(
            value = uiState.searchQuery,
            onValueChange = onSearchChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp)
                .testTag("search_station_input"),
            placeholder = { Text("Search station, district, corridor...", fontSize = 14.sp) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = ObsidianBlack
                )
            },
            trailingIcon = {
                if (uiState.searchQuery.isNotEmpty()) {
                    IconButton(onClick = { onSearchChange("") }) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Clear search",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = PureWhite,
                unfocusedContainerColor = PureWhite,
                focusedBorderColor = GreeneryPrimary,
                unfocusedBorderColor = BorderSubtle,
                focusedTextColor = ObsidianBlack,
                unfocusedTextColor = ObsidianBlack
            )
        )

        // Filter Chips: Risk Levels
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FilterChip(
                selected = uiState.selectedRiskFilter == null && !uiState.onlyBookmarked,
                onClick = {
                    onSelectRiskFilter(null)
                    if (uiState.onlyBookmarked) onToggleBookmarkFilter()
                },
                label = { Text("All (${uiState.allStations.size})", fontSize = 12.sp, fontWeight = FontWeight.SemiBold) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = ObsidianBlack,
                    selectedLabelColor = PureWhite,
                    containerColor = PureWhite,
                    labelColor = ObsidianBlack
                ),
                border = FilterChipDefaults.filterChipBorder(
                    enabled = true,
                    selected = uiState.selectedRiskFilter == null && !uiState.onlyBookmarked,
                    borderColor = BorderSubtle,
                    selectedBorderColor = ObsidianBlack
                )
            )

            FilterChip(
                selected = uiState.selectedRiskFilter == RiskLevel.CRITICAL,
                onClick = {
                    onSelectRiskFilter(if (uiState.selectedRiskFilter == RiskLevel.CRITICAL) null else RiskLevel.CRITICAL)
                },
                label = { Text("Critical (${uiState.criticalCount})", fontSize = 12.sp, fontWeight = FontWeight.Bold) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = RedAlertContainer,
                    selectedLabelColor = RedAlertPrimary,
                    containerColor = PureWhite,
                    labelColor = RedAlertPrimary
                ),
                border = FilterChipDefaults.filterChipBorder(
                    enabled = true,
                    selected = uiState.selectedRiskFilter == RiskLevel.CRITICAL,
                    borderColor = RedAlertBright.copy(alpha = 0.4f),
                    selectedBorderColor = RedAlertPrimary
                )
            )

            FilterChip(
                selected = uiState.selectedRiskFilter == RiskLevel.HIGH,
                onClick = {
                    onSelectRiskFilter(if (uiState.selectedRiskFilter == RiskLevel.HIGH) null else RiskLevel.HIGH)
                },
                label = { Text("Warning (${uiState.highCount})", fontSize = 12.sp, fontWeight = FontWeight.SemiBold) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = AlertAmberContainer,
                    selectedLabelColor = AlertAmberText,
                    containerColor = PureWhite,
                    labelColor = AlertAmberText
                ),
                border = FilterChipDefaults.filterChipBorder(
                    enabled = true,
                    selected = uiState.selectedRiskFilter == RiskLevel.HIGH,
                    borderColor = BorderSubtle,
                    selectedBorderColor = AlertAmber
                )
            )

            FilterChip(
                selected = uiState.onlyBookmarked,
                onClick = onToggleBookmarkFilter,
                label = { Text("Saved Points", fontSize = 12.sp, fontWeight = FontWeight.SemiBold) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = GreeneryContainer,
                    selectedLabelColor = GreeneryContainerText,
                    containerColor = PureWhite,
                    labelColor = GreeneryPrimary
                ),
                border = FilterChipDefaults.filterChipBorder(
                    enabled = true,
                    selected = uiState.onlyBookmarked,
                    borderColor = BorderSubtle,
                    selectedBorderColor = GreeneryPrimary
                )
            )
        }

        // State selector chips
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            item {
                FilterChip(
                    selected = uiState.selectedStateFilter == null,
                    onClick = { onSelectStateFilter(null) },
                    label = { Text("All States", fontSize = 11.sp, fontWeight = FontWeight.Medium) },
                    shape = RoundedCornerShape(8.dp),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = GreeneryContainer,
                        selectedLabelColor = GreeneryContainerText,
                        containerColor = PureWhite,
                        labelColor = ObsidianBlack
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        enabled = true,
                        selected = uiState.selectedStateFilter == null,
                        borderColor = BorderSubtle,
                        selectedBorderColor = GreeneryPrimary
                    )
                )
            }
            items(uiState.availableStates) { stateName ->
                FilterChip(
                    selected = uiState.selectedStateFilter == stateName,
                    onClick = {
                        onSelectStateFilter(if (uiState.selectedStateFilter == stateName) null else stateName)
                    },
                    label = { Text(stateName, fontSize = 11.sp, fontWeight = FontWeight.Medium) },
                    shape = RoundedCornerShape(8.dp),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = GreeneryContainer,
                        selectedLabelColor = GreeneryContainerText,
                        containerColor = PureWhite,
                        labelColor = ObsidianBlack
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        enabled = true,
                        selected = uiState.selectedStateFilter == stateName,
                        borderColor = BorderSubtle,
                        selectedBorderColor = GreeneryPrimary
                    )
                )
            }
        }

        // Stations Count and Status ribbon
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Active Telemetry Feeds (${uiState.filteredStations.size})",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = ObsidianBlack
            )

            if (uiState.statusMessage != null) {
                Text(
                    text = uiState.statusMessage,
                    fontSize = 11.sp,
                    color = GreeneryPrimary,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // LazyColumn with optimized stable keys
        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = GreeneryPrimary)
            }
        } else if (uiState.filteredStations.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.FilterList,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "No monitoring stations match this criteria",
                        fontSize = 15.sp,
                        color = ObsidianBlack,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Try clearing search keywords or state filters",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            LazyColumn(
                state = listState,
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("stations_lazy_list")
            ) {
                items(
                    items = uiState.filteredStations,
                    key = { it.station.id }
                ) { item ->
                    StationCard(
                        stationWithTelemetry = item,
                        onCardClick = { onStationClick(item.station.id) },
                        onBookmarkToggle = { onBookmarkToggle(item.station.id, item.station.isBookmarked) }
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(72.dp))
                }
            }
        }
    }
}

@Composable
private fun RegionalSummaryRibbon(
    criticalCount: Int,
    highCount: Int,
    moderateCount: Int,
    totalCount: Int,
    isSimulating: Boolean,
    onRefreshPulse: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(14.dp),
        color = PureWhite,
        border = BorderStroke(1.dp, BorderSubtle),
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Live Indicator Pill in Greenery
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (isSimulating) RedAlertContainer else GreeneryContainer)
                        .padding(horizontal = 7.dp, vertical = 4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .background(if (isSimulating) RedAlertPrimary else GreeneryPrimary, CircleShape)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = if (isSimulating) "LIVE PING" else "ONLINE",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isSimulating) RedAlertPrimary else GreeneryContainerText
                    )
                }

                // Critical summary in Red
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(RedAlertContainer)
                        .padding(horizontal = 7.dp, vertical = 3.dp)
                ) {
                    Box(modifier = Modifier.size(6.dp).background(RedAlertPrimary, CircleShape))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "$criticalCount Critical",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = RedAlertPrimary
                    )
                }

                // High summary
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(AlertAmberContainer)
                        .padding(horizontal = 7.dp, vertical = 3.dp)
                ) {
                    Box(modifier = Modifier.size(6.dp).background(AlertAmber, CircleShape))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "$highCount Warning",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = AlertAmberText
                    )
                }
            }

            IconButton(
                onClick = onRefreshPulse,
                modifier = Modifier
                    .size(36.dp)
                    .testTag("manual_pulse_btn")
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Simulate Telemetry Ping",
                    tint = ObsidianBlack,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
