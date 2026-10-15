package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.BatteryFull
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.RiskLevel
import com.example.data.model.StationWithLatestTelemetry
import com.example.data.model.TelemetryReadingEntity
import com.example.ui.components.RiskBadge
import com.example.ui.components.SparklineChart
import com.example.ui.components.TelemetryMetricChip
import com.example.ui.theme.AlertAmber
import com.example.ui.theme.AlertAmberContainer
import com.example.ui.theme.AlertAmberText
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.GreeneryContainer
import com.example.ui.theme.GreeneryContainerText
import com.example.ui.theme.GreeneryPrimary
import com.example.ui.theme.MetricPorePressure
import com.example.ui.theme.MetricRainfall
import com.example.ui.theme.MetricSeismic
import com.example.ui.theme.MetricTilt
import com.example.ui.theme.ObsidianBlack
import com.example.ui.theme.PureWhite
import com.example.ui.theme.RedAlertBright
import com.example.ui.theme.RedAlertContainer
import com.example.ui.theme.RedAlertDark
import com.example.ui.theme.RedAlertPrimary
import com.example.ui.theme.SurfaceClean

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StationDetailScreen(
    stationWithTelemetry: StationWithLatestTelemetry?,
    telemetryHistory: List<TelemetryReadingEntity>,
    onBackClick: () -> Unit,
    onBookmarkToggle: (String, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler {
        onBackClick()
    }

    if (stationWithTelemetry == null) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Station not found", color = ObsidianBlack)
        }
        return
    }

    val station = stationWithTelemetry.station
    val latest = stationWithTelemetry.latestReading
    val risk = stationWithTelemetry.evaluatedRiskLevel
    val baseFos = stationWithTelemetry.estimatedFactorOfSafety

    // Geotechnical interactive simulation state
    var simulatedAdditionalPorePressure by remember { mutableDoubleStateOf(0.0) }
    val effectivePorePressure = (latest?.porePressureKpa ?: 30.0) + simulatedAdditionalPorePressure
    val simulatedFos = remember(simulatedAdditionalPorePressure, baseFos) {
        val delta = (simulatedAdditionalPorePressure * 0.008)
        (baseFos - delta).coerceIn(0.35, 2.80)
    }

    val (fosBg, fosText) = when {
        simulatedFos < 1.0 -> Pair(RedAlertContainer, RedAlertPrimary)
        simulatedFos <= 1.3 -> Pair(AlertAmberContainer, AlertAmberText)
        else -> Pair(GreeneryContainer, GreeneryContainerText)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(PureWhite)
    ) {
        // Top App Bar
        Column {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = station.code,
                            fontSize = 12.sp,
                            color = GreeneryPrimary,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = station.name,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = ObsidianBlack,
                            maxLines = 1
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier.testTag("detail_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back to list",
                            tint = ObsidianBlack
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { onBookmarkToggle(station.id, station.isBookmarked) },
                        modifier = Modifier.testTag("detail_bookmark_btn")
                    ) {
                        Icon(
                            imageVector = if (station.isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = "Bookmark",
                            tint = if (station.isBookmarked) GreeneryPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PureWhite
                )
            )
            HorizontalDivider(color = BorderSubtle, thickness = 1.dp)
        }

        // Scrollable content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Hero Status Banner
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = PureWhite),
                border = BorderStroke(1.dp, if (risk == RiskLevel.CRITICAL) RedAlertBright.copy(alpha = 0.5f) else BorderSubtle),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RiskBadge(riskLevel = risk)

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(fosBg)
                                .padding(horizontal = 10.dp, vertical = 5.dp)
                        ) {
                            Text(
                                text = "Factor of Safety: ${String.format("%.2f", simulatedFos)}",
                                color = fosText,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 13.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = station.description,
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        color = ObsidianBlack
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = GreeneryPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "${station.district}, ${station.state} (${station.latitude}°, ${station.longitude}°)",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Geological Profile Specs Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = PureWhite),
                border = BorderStroke(1.dp, BorderSubtle),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Geotechnical Terrain Profile",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = ObsidianBlack
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    ProfileRow("Slope Angle", "${station.slopeAngleDeg}° steep")
                    ProfileRow("Elevation", "${station.elevationM.toInt()} meters ASL")
                    ProfileRow("Soil Type", station.soilType)
                    ProfileRow("Bedrock / Lithology", station.lithology)
                    ProfileRow("Drainage Density", station.drainageDensity)
                    ProfileRow("Pop. at Risk", "${station.populationAtRisk} residents")
                    ProfileRow("Critical Infra", station.criticalInfrastructure)
                }
            }

            // Real-time Sparkline Charts Section
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = PureWhite),
                border = BorderStroke(1.dp, BorderSubtle),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "12-Hour Telemetry History",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = ObsidianBlack
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(7.dp).background(GreeneryPrimary, CircleShape))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "13 Pings Synced",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = GreeneryPrimary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // 1. Tilt Inclinometer in Red Alert
                    val tiltHistory = telemetryHistory.map { it.inclinometerTiltDeg }
                    SparklineChart(
                        title = "Inclinometer Slope Tilt",
                        currentValue = String.format("%.2f", latest?.inclinometerTiltDeg ?: 0.0),
                        unit = "°",
                        dataPoints = if (tiltHistory.isNotEmpty()) tiltHistory else listOf(2.0, 2.5, 3.0),
                        lineColor = MetricTilt
                    )

                    HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = BorderSubtle)

                    // 2. Pore Water Pressure
                    val poreHistory = telemetryHistory.map { it.porePressureKpa }
                    SparklineChart(
                        title = "Pore Water Pressure",
                        currentValue = String.format("%.1f", latest?.porePressureKpa ?: 0.0),
                        unit = "kPa",
                        dataPoints = if (poreHistory.isNotEmpty()) poreHistory else listOf(40.0, 50.0, 60.0),
                        lineColor = MetricPorePressure
                    )

                    HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = BorderSubtle)

                    // 3. Accumulated Rainfall in Greenery
                    val rainHistory = telemetryHistory.map { it.rainfallAcc24hMm }
                    SparklineChart(
                        title = "24h Cumulative Precipitation",
                        currentValue = String.format("%.1f", latest?.rainfallAcc24hMm ?: 0.0),
                        unit = "mm",
                        dataPoints = if (rainHistory.isNotEmpty()) rainHistory else listOf(20.0, 50.0, 100.0),
                        lineColor = MetricRainfall
                    )

                    HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = BorderSubtle)

                    // 4. Seismic Acceleration PGA
                    val pgaHistory = telemetryHistory.map { it.seismicPgaGal }
                    SparklineChart(
                        title = "Seismic Micro-Tremor PGA",
                        currentValue = String.format("%.1f", latest?.seismicPgaGal ?: 0.0),
                        unit = "gal",
                        dataPoints = if (pgaHistory.isNotEmpty()) pgaHistory else listOf(1.0, 3.0, 8.0),
                        lineColor = MetricSeismic
                    )
                }
            }

            // Geotechnical Stability Simulator Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = PureWhite),
                border = BorderStroke(1.dp, BorderSubtle),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Interactive Slope Failure Simulator",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = ObsidianBlack
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Simulate heavy cloudburst pore-pressure surges to project slope slip threshold:",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Additional Surge: +${simulatedAdditionalPorePressure.toInt()} kPa",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = ObsidianBlack
                        )
                        Text(
                            text = "Projected FoS: ${String.format("%.2f", simulatedFos)}",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = fosText
                        )
                    }

                    Slider(
                        value = simulatedAdditionalPorePressure.toFloat(),
                        onValueChange = { simulatedAdditionalPorePressure = it.toDouble() },
                        valueRange = 0f..60f,
                        steps = 5,
                        colors = SliderDefaults.colors(
                            thumbColor = GreeneryPrimary,
                            activeTrackColor = GreeneryPrimary,
                            inactiveTrackColor = BorderSubtle
                        )
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "0 kPa (Live)", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(text = "+30 kPa (Heavy Rain)", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(text = "+60 kPa (Cloudburst)", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }

                    if (simulatedFos < 1.0) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(RedAlertContainer)
                                .border(BorderStroke(1.dp, RedAlertBright.copy(alpha = 0.5f)), RoundedCornerShape(8.dp))
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(imageVector = Icons.Default.Warning, contentDescription = null, tint = RedAlertPrimary)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "CRITICAL: Slope failure threshold exceeded! Immediate evacuation alert protocol triggered.",
                                fontSize = 12.sp,
                                color = RedAlertDark,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // Hardware & Telemetry Diagnostic Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = PureWhite),
                border = BorderStroke(1.dp, BorderSubtle),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Field Sensor Station Health",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = ObsidianBlack
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        TelemetryMetricChip(
                            label = "Solar/Battery",
                            value = "${latest?.batteryPct?.toInt() ?: 98}",
                            unit = "%",
                            icon = Icons.Default.BatteryFull,
                            accentColor = GreeneryPrimary,
                            modifier = Modifier.weight(1f)
                        )
                        TelemetryMetricChip(
                            label = "LoRa/Cellular",
                            value = "${latest?.signalStrengthDbm ?: -65}",
                            unit = "dBm",
                            icon = Icons.Default.Sensors,
                            accentColor = ObsidianBlack,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}

@Composable
private fun ProfileRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = ObsidianBlack
        )
    }
}
