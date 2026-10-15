package com.example.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "telemetry_readings",
    indices = [
        Index(value = ["location_id"]),
        Index(value = ["location_id", "timestamp"])
    ]
)
data class TelemetryReadingEntity(
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "location_id")
    val locationId: String,
    val timestamp: String,
    @ColumnInfo(name = "rainfall_rate_mm_hr")
    val rainfallRateMmHr: Double,
    @ColumnInfo(name = "rainfall_acc_24h_mm")
    val rainfallAcc24hMm: Double,
    @ColumnInfo(name = "pore_pressure_kpa")
    val porePressureKpa: Double,
    @ColumnInfo(name = "inclinometer_tilt_deg")
    val inclinometerTiltDeg: Double,
    @ColumnInfo(name = "seismic_pga_gal")
    val seismicPgaGal: Double,
    @ColumnInfo(name = "soil_moisture_pct")
    val soilMoisturePct: Double,
    @ColumnInfo(name = "battery_pct")
    val batteryPct: Double,
    @ColumnInfo(name = "signal_strength_dbm")
    val signalStrengthDbm: Int
)
