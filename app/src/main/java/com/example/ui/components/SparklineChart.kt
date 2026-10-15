package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SparklineChart(
    title: String,
    currentValue: String,
    unit: String,
    dataPoints: List<Double>,
    lineColor: Color,
    modifier: Modifier = Modifier,
    heightDp: Int = 90
) {
    val stats = remember(dataPoints) {
        if (dataPoints.isEmpty()) {
            Triple(0.0, 0.0, emptyList<Double>())
        } else {
            val min = dataPoints.minOrNull() ?: 0.0
            val max = dataPoints.maxOrNull() ?: 1.0
            val range = if (max == min) 1.0 else max - min
            Triple(min, max, dataPoints)
        }
    }

    val minVal = stats.first
    val maxVal = stats.second
    val points = stats.third

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "$currentValue $unit",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = lineColor
            )
        }

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(heightDp.dp)
                .padding(top = 8.dp, bottom = 4.dp)
        ) {
            if (points.size < 2) return@Canvas

            val width = size.width
            val height = size.height
            val stepX = width / (points.size - 1)
            val range = (maxVal - minVal).coerceAtLeast(0.001)

            val strokePath = Path()
            val fillPath = Path()

            points.forEachIndexed { index, value ->
                val x = index * stepX
                val normalizedY = ((value - minVal) / range).toFloat()
                // Invert Y since (0,0) is top-left
                val y = height - (normalizedY * (height - 12f)) - 6f

                if (index == 0) {
                    strokePath.moveTo(x, y)
                    fillPath.moveTo(x, height)
                    fillPath.lineTo(x, y)
                } else {
                    strokePath.lineTo(x, y)
                    fillPath.lineTo(x, y)
                }

                if (index == points.size - 1) {
                    fillPath.lineTo(x, height)
                    fillPath.close()
                }
            }

            // Draw Area Fill Gradient
            drawPath(
                path = fillPath,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        lineColor.copy(alpha = 0.35f),
                        lineColor.copy(alpha = 0.02f)
                    ),
                    startY = 0f,
                    endY = height
                )
            )

            // Draw Stroke Line
            drawPath(
                path = strokePath,
                color = lineColor,
                style = Stroke(
                    width = 2.5f.dp.toPx(),
                    cap = StrokeCap.Round
                )
            )

            // Draw current point beacon
            val lastX = (points.size - 1) * stepX
            val lastNormalizedY = ((points.last() - minVal) / range).toFloat()
            val lastY = height - (lastNormalizedY * (height - 12f)) - 6f

            drawCircle(
                color = lineColor,
                radius = 4f.dp.toPx(),
                center = Offset(lastX, lastY)
            )
            drawCircle(
                color = Color.White,
                radius = 2f.dp.toPx(),
                center = Offset(lastX, lastY)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "12h ago: ${String.format("%.1f", minVal)}",
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
            )
            Text(
                text = "Peak: ${String.format("%.1f", maxVal)} $unit",
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
            )
        }
    }
}
