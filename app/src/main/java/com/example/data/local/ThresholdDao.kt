package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.ThresholdConfig
import kotlinx.coroutines.flow.Flow

@Dao
interface ThresholdDao {
    @Query("SELECT * FROM threshold_config WHERE id = 1 LIMIT 1")
    fun getThresholdConfig(): Flow<ThresholdConfig?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveThresholdConfig(config: ThresholdConfig)
}
