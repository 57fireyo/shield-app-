package com.example.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.data.model.StationEntity
import com.example.data.model.TelemetryReadingEntity
import com.example.data.model.ThresholdConfig

@Database(
    entities = [
        StationEntity::class,
        TelemetryReadingEntity::class,
        ThresholdConfig::class
    ],
    version = 1,
    exportSchema = false
)
abstract class SlopeWatchDatabase : RoomDatabase() {
    abstract fun stationDao(): StationDao
    abstract fun telemetryDao(): TelemetryDao
    abstract fun thresholdDao(): ThresholdDao

    companion object {
        const val DATABASE_NAME = "slopewatch_ner.db"
    }
}
