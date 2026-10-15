package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.RiskLevel
import com.example.data.model.StationWithLatestTelemetry
import com.example.ui.theme.RiskCriticalRed
import com.example.ui.theme.RiskLowGreen
import com.example.ui.theme.RiskWarningAmber

enum class MatrixSort {
    CRITICAL_FIRST,
    SLOPE_STEEPNESS,
    POPULATION
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegionalMatrixScreen(
    stations: List<StationWithLatestTelemetry>,
    onStationClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var sortBy by remember { mutableStateOf(MatrixSort.CRITICAL_FIRST) }

    // Group stations by State
    val stateGroups = remember(stations, sortBy) {
        val grouped = stations.groupBy { it.station.state }
        when (sortBy) {
            MatrixSort.CRITICAL_FIRST -> grouped.toList().sortedByDescending { (_, list) ->
                list.count { it.evaluatedRiskLevel == RiskLevel.CRITICAL } * 10 + list.count { it.evaluatedRiskLevel == RiskLevel.HIGH }
            }
            MatrixSort.SLOPE_STEEPNESS -> grouped.toList().sortedByDescending { (_, list) ->
                list.maxOfOrNull { it.station.slopeAngleDeg } ?: 0.0
            }
            MatrixSort.POPULATION -> grouped.toList().sortedByDescending { (_, list) ->
                list.sumOf { it.station.populationAtRisk }
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Text(
                text = "North-East Regional Matrix",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Comparative Vulnerability & Critical Infrastructure Index",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Sort Filter Chips
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Sort,
                    contentDescription = "Sort",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(18.dp)
                )

                FilterChip(
                    selected = sortBy == MatrixSort.CRITICAL_FIRST,
                    onClick = { sortBy = MatrixSort.CRITICAL_FIRST },
                    label = { Text("Risk Level", fontSize = 11.sp) }
                )
                FilterChip(
                    selected = sortBy == MatrixSort.SLOPE_STEEPNESS,
                    onClick = { sortBy = MatrixSort.SLOPE_STEEPNESS },
                    label = { Text("Steepest Slopes", fontSize = 11.sp) }
                )
                FilterChip(
                    selected = sortBy == MatrixSort.POPULATION,
                    onClick = { sortBy = MatrixSort.POPULATION },
                    label = { Text("Population", fontSize = 11.sp) }
                )
            }
        }

        // State grouped list
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(stateGroups, key = { it.first }) { (stateName, stateStations) ->
                val criticals = stateStations.count { it.evaluatedRiskLevel == RiskLevel.CRITICAL }
                val highs = stateStations.count { it.evaluatedRiskLevel == RiskLevel.HIGH }
                val totalPop = stateStations.sumOf { it.station.populationAtRisk }
                val maxSlope = stateStations.maxOfOrNull { it.station.slopeAngleDeg } ?: 0.0
                val maxRain = stateStations.maxOfOrNull { it.latestReading?.rainfallAcc24hMm ?: 0.0 } ?: 0.0

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stateName,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                if (criticals > 0) {
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(RiskCriticalRed.copy(alpha = 0.15f))
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = "$criticals CRITICAL",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = RiskCriticalRed
                                        )
                                    }
                                }
                                if (highs > 0) {
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(RiskWarningAmber.copy(alpha = 0.15f))
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = "$highs WARNING",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = RiskWarningAmber
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // State quick stats row
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            StatColumn("Stations", "${stateStations.size}")
                            StatColumn("Max Slope", "${maxSlope}°")
                            StatColumn("Max 24h Rain", "${maxRain.toInt()}mm")
                            StatColumn("Pop. Exposed", "${totalPop / 1000}k")
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Station items in state
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            stateStations.forEach { item ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
                                        .padding(horizontal = 10.dp, vertical = 8.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = "${item.station.code} • ${item.station.name}",
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        Text(
                                            text = "Infra: ${item.station.criticalInfrastructure}",
                                            fontSize = 11.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(item.evaluatedRiskLevel.getColor().copy(alpha = 0.15f))
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = item.evaluatedRiskLevel.label,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = item.evaluatedRiskLevel.getColor()
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.height(72.dp))
            }
        }
    }
}

@Composable
private fun StatColumn(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(text = value, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
    }
}
