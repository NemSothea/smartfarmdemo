package com.smartfarm.android.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "events")
data class EventEntry(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val type: String = "ដំណាំ",
    val description: String = "",
    val dateMillis: Long,
    val hasReminder: Boolean = false,
    val reminderOffsetMinutes: Int = 30,
    val isDone: Boolean = false
)
