package com.example.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.data.model.*

@Database(
    entities = [
        UserProfile::class,
        CompanionProfile::class,
        AppSettings::class,
        LifeMoment::class,
        Goal::class,
        Habit::class,
        HabitLog::class,
        PlannerItem::class,
        FinanceAccount::class,
        FinanceTransaction::class,
        Budget::class,
        ImportantPerson::class,
        TimeCapsule::class,
        ChatMessage::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(AlongConverters::class)
abstract class AlongDatabase : RoomDatabase() {

    abstract fun alongDao(): AlongDao

    companion object {
        @Volatile
        private var INSTANCE: AlongDatabase? = null

        fun getDatabase(context: Context): AlongDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AlongDatabase::class.java,
                    "along_companion.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
