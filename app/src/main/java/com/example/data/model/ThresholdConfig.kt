package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "threshold_config")
data class ThresholdConfig(
    @PrimaryKey
    val id: Int = 1,
    val porePressureWarningKpa: Double = 50.0,
    val porePressureCriticalKpa: Double = 75.0,
    val tiltWarningDeg: Double = 2.5,
    val tiltCriticalDeg: Double = 4.0,
    val rainfall24hWarningMm: Double = 80.0,
    val rainfall24hCriticalMm: Double = 120.0,
    val seismicPgaWarningGal: Double = 8.0,
    val autoAlertsEnabled: Boolean = true
)
