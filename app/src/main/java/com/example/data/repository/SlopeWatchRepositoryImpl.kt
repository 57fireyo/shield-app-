package com.example.data.repository

import com.example.data.local.StationDao
import com.example.data.local.TelemetryDao
import com.example.data.local.ThresholdDao
import com.example.data.model.StationEntity
import com.example.data.model.StationWithLatestTelemetry
import com.example.data.model.TelemetryReadingEntity
import com.example.data.model.ThresholdConfig
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class SlopeWatchRepositoryImpl(
    private val stationDao: StationDao,
    private val telemetryDao: TelemetryDao,
    private val thresholdDao: ThresholdDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : SlopeWatchRepository {

    override fun getStationsWithTelemetry(): Flow<List<StationWithLatestTelemetry>> {
        return combine(
            stationDao.getAllStations(),
            telemetryDao.getLatestReadingPerStation()
        ) { stations, readings ->
            val readingsMap = readings.associateBy { it.locationId }
            stations.map { station ->
                StationWithLatestTelemetry(
                    station = station,
                    latestReading = readingsMap[station.id],
                    historicalReadingsCount = 13
                )
            }
        }.flowOn(ioDispatcher)
    }

    override fun getStationDetail(stationId: String): Flow<StationWithLatestTelemetry?> {
        return combine(
            stationDao.getStationById(stationId),
            telemetryDao.getLatestReadingForStation(stationId)
        ) { station, reading ->
            station?.let {
                StationWithLatestTelemetry(
                    station = it,
                    latestReading = reading
                )
            }
        }.flowOn(ioDispatcher)
    }

    override fun getStationTelemetryHistory(stationId: String): Flow<List<TelemetryReadingEntity>> {
        return telemetryDao.getReadingsForStation(stationId)
            .distinctUntilChanged()
            .flowOn(ioDispatcher)
    }

    override fun getThresholdConfig(): Flow<ThresholdConfig> {
        return thresholdDao.getThresholdConfig()
            .map { it ?: SeedDataProvider.getDefaultThresholds() }
            .distinctUntilChanged()
            .flowOn(ioDispatcher)
    }

    override suspend fun saveThresholdConfig(config: ThresholdConfig) = withContext(ioDispatcher) {
        thresholdDao.saveThresholdConfig(config)
    }

    override suspend fun toggleBookmark(stationId: String, currentVal: Boolean) = withContext(ioDispatcher) {
        stationDao.updateBookmark(stationId, !currentVal)
    }

    override suspend fun simulateTelemetryPulse() = withContext(ioDispatcher) {
        val stations = SeedDataProvider.getInitialStations()
        val timeFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
        val nowIso = timeFormat.format(Date())

        for (station in stations) {
            val existing = telemetryDao.getLatestReadingOnce(station.id) 
                ?: SeedDataProvider.getInitialTelemetry().findLast { it.locationId == station.id }
            if (existing != null) {
                // Introduce slight realistic jitter simulating live sensor telemetry ping
                val deltaRain = (Math.random() * 0.8 - 0.2).coerceAtLeast(-0.5)
                val deltaPore = (Math.random() * 0.6 - 0.2).coerceAtLeast(-0.4)
                val deltaTilt = (Math.random() * 0.04 - 0.01).coerceAtLeast(-0.02)

                val newReading = TelemetryReadingEntity(
                    id = "pulse-${station.id}-${System.currentTimeMillis()}",
                    locationId = station.id,
                    timestamp = nowIso,
                    rainfallRateMmHr = (existing.rainfallRateMmHr + deltaRain).coerceAtLeast(0.0),
                    rainfallAcc24hMm = (existing.rainfallAcc24hMm + (deltaRain * 0.5)).coerceAtLeast(0.0),
                    porePressureKpa = (existing.porePressureKpa + deltaPore).coerceAtLeast(10.0),
                    inclinometerTiltDeg = (existing.inclinometerTiltDeg + deltaTilt).coerceAtLeast(0.1),
                    seismicPgaGal = (existing.seismicPgaGal + (Math.random() * 0.3 - 0.1)).coerceAtLeast(0.2),
                    soilMoisturePct = (existing.soilMoisturePct + (deltaPore * 0.2)).coerceIn(20.0, 99.0),
                    batteryPct = (existing.batteryPct - 0.05).coerceAtLeast(10.0),
                    signalStrengthDbm = (-60 - (Math.random() * 10).toInt())
                )
                telemetryDao.insertReading(newReading)
            }
        }
    }

    override suspend fun resetToDefaultSeed() = withContext(ioDispatcher) {
        stationDao.insertStations(SeedDataProvider.getInitialStations())
        telemetryDao.insertReadings(SeedDataProvider.getInitialTelemetry())
        thresholdDao.saveThresholdConfig(SeedDataProvider.getDefaultThresholds())
    }

    override suspend fun initializeSeedDataIfEmpty() = withContext(ioDispatcher) {
        val count = stationDao.getStationCount()
        if (count == 0) {
            resetToDefaultSeed()
        }
    }
}
