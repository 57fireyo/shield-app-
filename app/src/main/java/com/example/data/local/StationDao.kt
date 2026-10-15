package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.StationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StationDao {
    @Query("SELECT * FROM stations ORDER BY CASE risk_level WHEN 'CRITICAL' THEN 1 WHEN 'HIGH' THEN 2 WHEN 'MODERATE' THEN 3 ELSE 4 END ASC, name ASC")
    fun getAllStations(): Flow<List<StationEntity>>

    @Query("SELECT * FROM stations WHERE id = :stationId LIMIT 1")
    fun getStationById(stationId: String): Flow<StationEntity?>

    @Query("SELECT * FROM stations WHERE state = :state")
    fun getStationsByState(state: String): Flow<List<StationEntity>>

    @Query("SELECT * FROM stations WHERE is_bookmarked = 1")
    fun getBookmarkedStations(): Flow<List<StationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStations(stations: List<StationEntity>)

    @Query("UPDATE stations SET is_bookmarked = :isBookmarked WHERE id = :stationId")
    suspend fun updateBookmark(stationId: String, isBookmarked: Boolean)

    @Query("UPDATE stations SET risk_level = :riskLevel, station_status = :status WHERE id = :stationId")
    suspend fun updateStatus(stationId: String, riskLevel: String, status: String)

    @Query("SELECT COUNT(*) FROM stations")
    suspend fun getStationCount(): Int
}
