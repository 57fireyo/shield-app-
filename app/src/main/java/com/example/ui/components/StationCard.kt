package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Terrain
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.RiskLevel
import com.example.data.model.StationWithLatestTelemetry
import com.example.ui.theme.AlertAmber
import com.example.ui.theme.AlertAmberContainer
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.GreeneryContainer
import com.example.ui.theme.GreeneryContainerText
import com.example.ui.theme.GreeneryPrimary
import com.example.ui.theme.MetricPorePressure
import com.example.ui.theme.MetricRainfall
import com.example.ui.theme.MetricTilt
import com.example.ui.theme.ObsidianBlack
import com.example.ui.theme.PureBlack
import com.example.ui.theme.PureWhite
import com.example.ui.theme.RedAlertBright
import com.example.ui.theme.RedAlertContainer
import com.example.ui.theme.RedAlertDark
import com.example.ui.theme.RedAlertPrimary

@Composable
fun StationCard(
    stationWithTelemetry: StationWithLatestTelemetry,
    onCardClick: () -> Unit,
    onBookmarkToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val station = stationWithTelemetry.station
    val reading = stationWithTelemetry.latestReading
    val risk = stationWithTelemetry.evaluatedRiskLevel
    val fos = stationWithTelemetry.estimatedFactorOfSafety

    val (fosBg, fosText) = when {
        fos < 1.0 -> Pair(RedAlertContainer, RedAlertPrimary)
        fos <= 1.3 -> Pair(AlertAmberContainer, AlertAmber)
        else -> Pair(GreeneryContainer, GreeneryContainerText)
    }

    val cardBorderColor = when (risk) {
        RiskLevel.CRITICAL -> RedAlertBright
        RiskLevel.HIGH -> RedAlertPrimary.copy(alpha = 0.5f)
        else -> BorderSubtle
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("station_card_${station.id}")
            .clickable(onClick = onCardClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = PureWhite
        ),
        border = BorderStroke(
            width = if (risk == RiskLevel.CRITICAL) 1.5.dp else 1.dp,
            color = cardBorderColor
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header: Code, Badges, Bookmark
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    // Black & Greenery station code badge
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(ObsidianBlack)
                            .padding(horizontal = 7.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = station.code,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = PureWhite
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    RiskBadge(riskLevel = risk)
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Factor of Safety badge
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(fosBg)
                            .padding(horizontal = 7.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = "FoS: $fos",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = fosText
                        )
                    }

                    IconButton(
                        onClick = onBookmarkToggle,
                        modifier = Modifier
                            .size(38.dp)
                            .testTag("bookmark_btn_${station.id}")
                    ) {
                        Icon(
                            imageVector = if (station.isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = if (station.isBookmarked) "Unbookmark" else "Bookmark",
                            tint = if (station.isBookmarked) GreeneryPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Station Name in crisp bold Obsidian Black
            Text(
                text = station.name,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = ObsidianBlack,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            // State & District & Slope
            Spacer(modifier = Modifier.height(3.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "${station.district}, ${station.state}",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "•",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Slope: ${station.slopeAngleDeg}° (${station.elevationM.toInt()}m)",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = GreeneryPrimary
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Telemetry Metric Grid
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val tilt = reading?.inclinometerTiltDeg ?: 0.0
                TelemetryMetricChip(
                    label = "Inclinometer Tilt",
                    value = String.format("%.2f", tilt),
                    unit = "°",
                    icon = Icons.Default.Speed,
                    accentColor = if (tilt >= 4.0) RedAlertPrimary else MetricTilt,
                    modifier = Modifier.weight(1f),
                    isWarning = tilt >= 4.0
                )
                val pore = reading?.porePressureKpa ?: 0.0
                TelemetryMetricChip(
                    label = "Pore Pressure",
                    value = String.format("%.1f", pore),
                    unit = "kPa",
                    icon = Icons.Default.WaterDrop,
                    accentColor = if (pore >= 70.0) RedAlertPrimary else MetricPorePressure,
                    modifier = Modifier.weight(1f),
                    isWarning = pore >= 70.0
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val rain = reading?.rainfallAcc24hMm ?: 0.0
                TelemetryMetricChip(
                    label = "24h Rainfall",
                    value = String.format("%.1f", rain),
                    unit = "mm",
                    icon = Icons.Default.WaterDrop,
                    accentColor = if (rain >= 120.0) RedAlertPrimary else MetricRainfall,
                    modifier = Modifier.weight(1f),
                    isWarning = rain >= 120.0
                )
                TelemetryMetricChip(
                    label = "Lithology",
                    value = station.lithology.take(12) + "…",
                    unit = "",
                    icon = Icons.Default.Terrain,
                    accentColor = ObsidianBlack,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Critical Infrastructure alert banner
            val isCritical = risk == RiskLevel.CRITICAL
            val infraBg = if (isCritical) RedAlertContainer else GreeneryContainer.copy(alpha = 0.5f)
            val infraTextColor = if (isCritical) RedAlertDark else GreeneryContainerText
            val infraIconColor = if (isCritical) RedAlertPrimary else GreeneryPrimary

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(infraBg)
                    .padding(horizontal = 10.dp, vertical = 7.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = null,
                        tint = infraIconColor,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Infra: ${station.criticalInfrastructure}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = infraTextColor,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = "View telemetry",
                    tint = if (isCritical) RedAlertPrimary else GreeneryPrimary,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}
