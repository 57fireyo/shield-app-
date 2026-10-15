package com.example.data.model

import androidx.compose.ui.graphics.Color
import com.example.ui.theme.AlertAmber
import com.example.ui.theme.AlertAmberContainer
import com.example.ui.theme.AlertAmberText
import com.example.ui.theme.GreeneryContainer
import com.example.ui.theme.GreeneryContainerText
import com.example.ui.theme.GreeneryPrimary
import com.example.ui.theme.GreeneryVibrant
import com.example.ui.theme.RedAlertContainer
import com.example.ui.theme.RedAlertContainerText
import com.example.ui.theme.RedAlertDark
import com.example.ui.theme.RedAlertPrimary

enum class RiskLevel(val label: String, val severityRank: Int) {
    CRITICAL("CRITICAL", 4),
    HIGH("HIGH", 3),
    MODERATE("MODERATE", 2),
    LOW("LOW", 1);

    fun getColor(): Color = when (this) {
        CRITICAL -> RedAlertPrimary
        HIGH -> RedAlertDark
        MODERATE -> AlertAmber
        LOW -> GreeneryPrimary
    }

    fun getContainerColor(): Color = when (this) {
        CRITICAL -> RedAlertContainer
        HIGH -> RedAlertContainer
        MODERATE -> AlertAmberContainer
        LOW -> GreeneryContainer
    }

    fun getTextColor(): Color = when (this) {
        CRITICAL -> RedAlertContainerText
        HIGH -> RedAlertContainerText
        MODERATE -> AlertAmberText
        LOW -> GreeneryContainerText
    }

    companion object {
        fun fromString(value: String): RiskLevel {
            return entries.firstOrNull { it.name.equals(value.trim(), ignoreCase = true) } ?: MODERATE
        }
    }
}
