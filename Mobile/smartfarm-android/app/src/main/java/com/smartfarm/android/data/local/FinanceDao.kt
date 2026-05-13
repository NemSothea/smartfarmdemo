package com.smartfarm.android.data.local

import androidx.room.*
import com.smartfarm.android.data.local.entity.FinanceEntry
import com.smartfarm.android.data.local.entity.FinanceType
import kotlinx.coroutines.flow.Flow

@Dao
interface FinanceDao {
    @Query("SELECT * FROM finance_entries ORDER BY dateMillis DESC")
    fun getAll(): Flow<List<FinanceEntry>>

    @Query("SELECT * FROM finance_entries ORDER BY dateMillis DESC")
    suspend fun getAllOnce(): List<FinanceEntry>

    @Query("SELECT * FROM finance_entries WHERE type = :type ORDER BY dateMillis DESC")
    fun getByType(type: FinanceType): Flow<List<FinanceEntry>>

    @Query("SELECT COALESCE(SUM(amount), 0) FROM finance_entries WHERE type = 'INCOME'")
    fun totalIncome(): Flow<Double>

    @Query("SELECT COALESCE(SUM(amount), 0) FROM finance_entries WHERE type = 'EXPENSE'")
    fun totalExpense(): Flow<Double>

    @Query("SELECT COALESCE(SUM(amount), 0) FROM finance_entries WHERE type = 'INCOME' AND dateMillis >= :from AND dateMillis < :to")
    fun monthIncome(from: Long, to: Long): Flow<Double>

    @Query("SELECT COALESCE(SUM(amount), 0) FROM finance_entries WHERE type = 'EXPENSE' AND dateMillis >= :from AND dateMillis < :to")
    fun monthExpense(from: Long, to: Long): Flow<Double>

    @Query("SELECT * FROM finance_entries ORDER BY dateMillis DESC LIMIT :limit")
    fun getRecent(limit: Int): Flow<List<FinanceEntry>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: FinanceEntry)

    @Update
    suspend fun update(entry: FinanceEntry)

    @Delete
    suspend fun delete(entry: FinanceEntry)

    @Query("DELETE FROM finance_entries")
    suspend fun deleteAll()
}
