package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material.icons.filled.Terrain
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.CharcoalMuted
import com.example.ui.theme.GreeneryContainer
import com.example.ui.theme.GreeneryPrimary
import com.example.ui.theme.ObsidianBlack
import com.example.ui.theme.PureWhite
import com.example.ui.theme.SurfaceClean
import com.example.ui.viewmodel.SlopeWatchViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainNavigationScreen(
    viewModel: SlopeWatchViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }

    // If a station is selected, display StationDetailScreen
    if (uiState.selectedStationId != null) {
        StationDetailScreen(
            stationWithTelemetry = uiState.selectedStationDetail,
            telemetryHistory = uiState.selectedStationHistory,
            onBackClick = { viewModel.onSelectStation(null) },
            onBookmarkToggle = { id, currentVal -> viewModel.toggleBookmark(id, currentVal) }
        )
        return
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = PureWhite,
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(GreeneryContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Terrain,
                                    contentDescription = null,
                                    tint = GreeneryPrimary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "SlopeWatch NER",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 0.3.sp,
                                color = ObsidianBlack
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = PureWhite
                    )
                )
                HorizontalDivider(color = BorderSubtle, thickness = 1.dp)
            }
        },
        bottomBar = {
            Column {
                HorizontalDivider(color = BorderSubtle, thickness = 1.dp)
                NavigationBar(
                    containerColor = PureWhite,
                    contentColor = CharcoalMuted,
                    tonalElevation = 0.dp
                ) {
                    NavigationBarItem(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        icon = { Icon(Icons.Default.Sensors, contentDescription = "Telemetry") },
                        label = { Text("Telemetry", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                        modifier = Modifier.testTag("nav_telemetry_tab"),
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = GreeneryPrimary,
                            selectedTextColor = GreeneryPrimary,
                            indicatorColor = GreeneryContainer,
                            unselectedIconColor = CharcoalMuted,
                            unselectedTextColor = CharcoalMuted
                        )
                    )

                    NavigationBarItem(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        icon = { Icon(Icons.Default.GridView, contentDescription = "Regional Matrix") },
                        label = { Text("NER Matrix", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                        modifier = Modifier.testTag("nav_matrix_tab"),
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = GreeneryPrimary,
                            selectedTextColor = GreeneryPrimary,
                            indicatorColor = GreeneryContainer,
                            unselectedIconColor = CharcoalMuted,
                            unselectedTextColor = CharcoalMuted
                        )
                    )

                    NavigationBarItem(
                        selected = selectedTab == 2,
                        onClick = { selectedTab = 2 },
                        icon = { Icon(Icons.Default.Tune, contentDescription = "Thresholds") },
                        label = { Text("Thresholds", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                        modifier = Modifier.testTag("nav_thresholds_tab"),
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = GreeneryPrimary,
                            selectedTextColor = GreeneryPrimary,
                            indicatorColor = GreeneryContainer,
                            unselectedIconColor = CharcoalMuted,
                            unselectedTextColor = CharcoalMuted
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(PureWhite)
        ) {
            when (selectedTab) {
                0 -> DashboardScreen(
                    uiState = uiState,
                    onSearchChange = { viewModel.onSearchQueryChange(it) },
                    onSelectStateFilter = { viewModel.onSelectStateFilter(it) },
                    onSelectRiskFilter = { viewModel.onSelectRiskFilter(it) },
                    onToggleBookmarkFilter = { viewModel.onToggleBookmarkFilter() },
                    onStationClick = { viewModel.onSelectStation(it) },
                    onBookmarkToggle = { id, currentVal -> viewModel.toggleBookmark(id, currentVal) },
                    onSimulatePulse = { viewModel.triggerSimulationPulse() }
                )
                1 -> RegionalMatrixScreen(
                    stations = uiState.allStations,
                    onStationClick = { viewModel.onSelectStation(it) }
                )
                2 -> ThresholdSettingsScreen(
                    currentConfig = uiState.thresholdConfig,
                    isSimulationActive = uiState.isSimulationActive,
                    onSaveThresholds = { viewModel.saveThresholds(it) },
                    onSimulatePulse = { viewModel.triggerSimulationPulse() },
                    onToggleAutoSimulation = { viewModel.toggleAutoSimulation(it) },
                    onResetBaseline = { viewModel.resetToBaseline() }
                )
            }
        }
    }
}
