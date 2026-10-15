package com.example.data.repository

import com.example.data.model.StationEntity
import com.example.data.model.StationWithLatestTelemetry
import com.example.data.model.TelemetryReadingEntity
import com.example.data.model.ThresholdConfig
import kotlinx.coroutines.flow.Flow

interface SlopeWatchRepository {
    fun getStationsWithTelemetry(): Flow<List<StationWithLatestTelemetry>>
    fun getStationDetail(stationId: String): Flow<StationWithLatestTelemetry?>
    fun getStationTelemetryHistory(stationId: String): Flow<List<TelemetryReadingEntity>>
    fun getThresholdConfig(): Flow<ThresholdConfig>
    suspend fun saveThresholdConfig(config: ThresholdConfig)
    suspend fun toggleBookmark(stationId: String, currentVal: Boolean)
    suspend fun simulateTelemetryPulse()
    suspend fun resetToDefaultSeed()
    suspend fun initializeSeedDataIfEmpty()
}
