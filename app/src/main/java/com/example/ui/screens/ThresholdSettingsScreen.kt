package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ThresholdConfig
import com.example.ui.theme.MetricPorePressure
import com.example.ui.theme.MetricRainfall
import com.example.ui.theme.MetricTilt
import com.example.ui.theme.RiskCriticalRed
import com.example.ui.theme.RiskWarningAmber

@Composable
fun ThresholdSettingsScreen(
    currentConfig: ThresholdConfig,
    isSimulationActive: Boolean,
    onSaveThresholds: (ThresholdConfig) -> Unit,
    onSimulatePulse: () -> Unit,
    onToggleAutoSimulation: (Boolean) -> Unit,
    onResetBaseline: () -> Unit,
    modifier: Modifier = Modifier
) {
    var poreWarning by remember(currentConfig) { mutableDoubleStateOf(currentConfig.porePressureWarningKpa) }
    var poreCritical by remember(currentConfig) { mutableDoubleStateOf(currentConfig.porePressureCriticalKpa) }
    var tiltWarning by remember(currentConfig) { mutableDoubleStateOf(currentConfig.tiltWarningDeg) }
    var tiltCritical by remember(currentConfig) { mutableDoubleStateOf(currentConfig.tiltCriticalDeg) }
    var rainWarning by remember(currentConfig) { mutableDoubleStateOf(currentConfig.rainfall24hWarningMm) }
    var rainCritical by remember(currentConfig) { mutableDoubleStateOf(currentConfig.rainfall24hCriticalMm) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Title
        Column {
            Text(
                text = "Alert Thresholds & Diagnostics",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Calibrate Geological Early-Warning triggers and telemetry simulation",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Live Simulation Card
        Card(
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
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Auto-Telemetry Pulse Stream",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Emulates field sensor updates every 4s",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Switch(
                        checked = isSimulationActive,
                        onCheckedChange = onToggleAutoSimulation,
                        modifier = Modifier.testTag("auto_simulation_switch"),
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = MaterialTheme.colorScheme.primary,
                            checkedTrackColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = onSimulatePulse,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("single_pulse_btn"),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Icon(imageVector = Icons.Default.Sensors, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Send Ping", fontSize = 13.sp)
                    }

                    OutlinedButton(
                        onClick = onResetBaseline,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("reset_baseline_btn")
                    ) {
                        Icon(imageVector = Icons.Default.RestartAlt, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Reset Baseline", fontSize = 13.sp)
                    }
                }
            }
        }

        // Threshold Sliders Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Threshold Triggers Calibration",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Pore Water Pressure Critical
                ThresholdSlider(
                    title = "Pore Pressure Critical",
                    currentValue = poreCritical,
                    unit = "kPa",
                    min = 40.0,
                    max = 100.0,
                    color = MetricPorePressure,
                    onValueChange = { poreCritical = it }
                )

                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))

                // Inclinometer Tilt Critical
                ThresholdSlider(
                    title = "Slope Tilt Critical Threshold",
                    currentValue = tiltCritical,
                    unit = "°",
                    min = 2.0,
                    max = 8.0,
                    color = MetricTilt,
                    onValueChange = { tiltCritical = it }
                )

                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))

                // 24h Precipitation Trigger
                ThresholdSlider(
                    title = "24-Hour Rainfall Warning Limit",
                    currentValue = rainWarning,
                    unit = "mm",
                    min = 40.0,
                    max = 200.0,
                    color = MetricRainfall,
                    onValueChange = { rainWarning = it }
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        onSaveThresholds(
                            currentConfig.copy(
                                porePressureCriticalKpa = poreCritical,
                                tiltCriticalDeg = tiltCritical,
                                rainfall24hWarningMm = rainWarning
                            )
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("save_thresholds_btn"),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Icon(imageVector = Icons.Default.Check, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Apply & Persist Thresholds", fontWeight = FontWeight.Bold)
                }
            }
        }

        // Architecture & Stack Diagnostic Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "System Diagnostics & Architecture",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(8.dp))
                DiagnosticRow("DI Engine", "Koin 3.5.6 (Singleton scope)")
                DiagnosticRow("Local Storage", "Room 2.7.0 (SQLite with indices)")
                DiagnosticRow("UI Framework", "Jetpack Compose M3 (Declarative)")
                DiagnosticRow("State Lifecycle", "StateFlow (WhileSubscribed 5s)")
                DiagnosticRow("Frame Rate Target", "60 / 120 FPS Native")
            }
        }

        Spacer(modifier = Modifier.height(72.dp))
    }
}

@Composable
private fun ThresholdSlider(
    title: String,
    currentValue: Double,
    unit: String,
    min: Double,
    max: Double,
    color: androidx.compose.ui.graphics.Color,
    onValueChange: (Double) -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = title, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(
                text = "${String.format("%.1f", currentValue)} $unit",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }
        Slider(
            value = currentValue.toFloat(),
            onValueChange = { onValueChange(Math.round(it * 10.0) / 10.0) },
            valueRange = min.toFloat()..max.toFloat(),
            colors = SliderDefaults.colors(
                thumbColor = color,
                activeTrackColor = color
            )
        )
    }
}

@Composable
private fun DiagnosticRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(text = value, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurface)
    }
}
