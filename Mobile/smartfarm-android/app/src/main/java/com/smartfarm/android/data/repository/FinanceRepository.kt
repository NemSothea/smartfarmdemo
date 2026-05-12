package com.smartfarm.android.data.repository

import com.smartfarm.android.data.local.FinanceDao
import com.smartfarm.android.data.local.entity.FinanceEntry
import com.smartfarm.android.data.local.entity.FinanceType
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FinanceRepository @Inject constructor(private val dao: FinanceDao) {
    fun getAll(): Flow<List<FinanceEntry>> = dao.getAll()
    suspend fun getAllOnce(): List<FinanceEntry> = dao.getAllOnce()
    fun getByType(type: FinanceType): Flow<List<FinanceEntry>> = dao.getByType(type)
    fun totalIncome(): Flow<Double> = dao.totalIncome()
    fun totalExpense(): Flow<Double> = dao.totalExpense()
    suspend fun save(entry: FinanceEntry) = if (entry.id == 0) dao.insert(entry) else dao.update(entry)
    suspend fun delete(entry: FinanceEntry) = dao.delete(entry)
    suspend fun deleteAll() = dao.deleteAll()
}
