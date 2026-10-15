package com.example.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stations")
data class StationEntity(
    @PrimaryKey
    val id: String,
    val code: String,
    val name: String,
    val state: String,
    val district: String,
    val latitude: Double,
    val longitude: Double,
    @ColumnInfo(name = "elevation_m")
    val elevationM: Double,
    @ColumnInfo(name = "slope_angle_deg")
    val slopeAngleDeg: Double,
    @ColumnInfo(name = "soil_type")
    val soilType: String,
    val lithology: String,
    @ColumnInfo(name = "drainage_density")
    val drainageDensity: String,
    @ColumnInfo(name = "risk_level")
    val riskLevel: String,
    val description: String,
    @ColumnInfo(name = "population_at_risk")
    val populationAtRisk: Int,
    @ColumnInfo(name = "critical_infrastructure")
    val criticalInfrastructure: String,
    @ColumnInfo(name = "station_status")
    val stationStatus: String,
    @ColumnInfo(name = "is_bookmarked")
    val isBookmarked: Boolean = false
)
