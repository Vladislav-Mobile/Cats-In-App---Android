package com.example.catsinapp.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FeedingLogDao {

    @Query("SELECT * FROM feeding_logs ORDER BY dateKey DESC, time ASC")
    suspend fun getAllLogs(): List<FeedingLogEntity>

    // Flow-версия: автоматически эмитит при любом изменении таблицы
    // (в том числе при SQL-запросах из DB Inspector)
    @Query("SELECT * FROM feeding_logs ORDER BY dateKey DESC, time ASC")
    fun observeAllLogs(): Flow<List<FeedingLogEntity>>

    @Query("SELECT * FROM feeding_logs WHERE dateKey = :dateKey ORDER BY time ASC")
    suspend fun getLogsByDate(dateKey: String): List<FeedingLogEntity>

    @Query("SELECT DISTINCT dateKey FROM feeding_logs")
    suspend fun getAllDates(): List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: FeedingLogEntity)

    @Query("DELETE FROM feeding_logs WHERE id = :logId")
    suspend fun deleteLog(logId: Int)

    @Query("SELECT COUNT(*) FROM feeding_logs")
    suspend fun totalCount(): Int
}
