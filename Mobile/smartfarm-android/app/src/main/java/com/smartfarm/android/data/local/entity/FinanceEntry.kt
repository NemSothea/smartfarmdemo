package com.smartfarm.android.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class FinanceType { INCOME, EXPENSE }

@Entity(tableName = "finance_entries")
data class FinanceEntry(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val amount: Double,
    val type: FinanceType,
    val category: String,
    val note: String = "",
    val dateMillis: Long = System.currentTimeMillis()
)
