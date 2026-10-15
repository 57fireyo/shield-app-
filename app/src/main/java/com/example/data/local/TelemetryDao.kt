package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.TelemetryReadingEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TelemetryDao {
    @Query("SELECT * FROM telemetry_readings WHERE location_id = :locationId ORDER BY timestamp ASC")
    fun getReadingsForStation(locationId: String): Flow<List<TelemetryReadingEntity>>

    @Query("SELECT * FROM telemetry_readings WHERE location_id = :locationId ORDER BY timestamp DESC LIMIT 1")
    fun getLatestReadingForStation(locationId: String): Flow<TelemetryReadingEntity?>

    @Query("""
        SELECT * FROM telemetry_readings
        WHERE id IN (
            SELECT id FROM telemetry_readings t1
            WHERE timestamp = (
                SELECT MAX(timestamp) FROM telemetry_readings t2 WHERE t2.location_id = t1.location_id
            )
        )
    """)
    fun getLatestReadingPerStation(): Flow<List<TelemetryReadingEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReadings(readings: List<TelemetryReadingEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReading(reading: TelemetryReadingEntity)

    @Query("SELECT COUNT(*) FROM telemetry_readings")
    suspend fun getReadingCount(): Int
}
