package com.smartfarm.android.data.repository

import com.smartfarm.android.data.local.EventDao
import com.smartfarm.android.data.local.entity.EventEntry
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventRepository @Inject constructor(private val dao: EventDao) {
    fun getAll(): Flow<List<EventEntry>> = dao.getAll()
    suspend fun getAllOnce(): List<EventEntry> = dao.getAllOnce()
    fun getInRange(startMillis: Long, endMillis: Long): Flow<List<EventEntry>> = dao.getInRange(startMillis, endMillis)
    fun getWithReminders(): Flow<List<EventEntry>> = dao.getWithReminders()
    suspend fun save(event: EventEntry) = if (event.id == 0) dao.insert(event) else dao.update(event)
    suspend fun delete(event: EventEntry) = dao.delete(event)
    suspend fun deleteAll() = dao.deleteAll()
}
