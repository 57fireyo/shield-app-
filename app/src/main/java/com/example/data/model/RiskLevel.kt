package com.example.data.model

import androidx.compose.ui.graphics.Color
import com.example.ui.theme.RiskCriticalRed
import com.example.ui.theme.RiskLowGreen
import com.example.ui.theme.RiskModerateYellow
import com.example.ui.theme.RiskWarningAmber

enum class RiskLevel(val label: String, val severityRank: Int) {
    CRITICAL("CRITICAL", 4),
    HIGH("HIGH", 3),
    MODERATE("MODERATE", 2),
    LOW("LOW", 1);

    fun getColor(): Color = when (this) {
        CRITICAL -> RiskCriticalRed
        HIGH -> RiskWarningAmber
        MODERATE -> RiskModerateYellow
        LOW -> RiskLowGreen
    }

    companion object {
        fun fromString(value: String): RiskLevel {
            return entries.firstOrNull { it.name.equals(value.trim(), ignoreCase = true) } ?: MODERATE
        }
    }
}
