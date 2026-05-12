package com.smartfarm.android.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.smartfarm.android.data.local.entity.EventEntry
import com.smartfarm.android.data.local.entity.FinanceEntry
import com.smartfarm.android.data.local.entity.FinanceType

class Converters {
    @TypeConverter fun fromFinanceType(value: FinanceType): String = value.name
    @TypeConverter fun toFinanceType(value: String): FinanceType = FinanceType.valueOf(value)
}

@Database(
    entities = [FinanceEntry::class, EventEntry::class],
    version = 3,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun financeDao(): FinanceDao
    abstract fun eventDao(): EventDao
}
