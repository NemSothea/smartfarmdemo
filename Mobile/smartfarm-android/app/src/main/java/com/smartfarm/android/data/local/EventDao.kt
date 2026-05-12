package com.smartfarm.android.data.local

import androidx.room.*
import com.smartfarm.android.data.local.entity.EventEntry
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {
    @Query("SELECT * FROM events ORDER BY dateMillis ASC")
    fun getAll(): Flow<List<EventEntry>>

    @Query("SELECT * FROM events ORDER BY dateMillis ASC")
    suspend fun getAllOnce(): List<EventEntry>

    @Query("SELECT * FROM events WHERE dateMillis BETWEEN :startMillis AND :endMillis ORDER BY dateMillis ASC")
    fun getInRange(startMillis: Long, endMillis: Long): Flow<List<EventEntry>>

    @Query("SELECT * FROM events WHERE hasReminder = 1 ORDER BY dateMillis ASC")
    fun getWithReminders(): Flow<List<EventEntry>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(event: EventEntry)

    @Update
    suspend fun update(event: EventEntry)

    @Delete
    suspend fun delete(event: EventEntry)

    @Query("DELETE FROM events")
    suspend fun deleteAll()
}
