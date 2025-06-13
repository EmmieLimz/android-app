package com.nipa.healthcaremobile.persistence

import android.content.Context // Added
import androidx.room.Database
import androidx.room.Room // Added
import androidx.room.RoomDatabase
import com.nipa.healthcaremobile.model.auth.User
import com.nipa.healthcaremobile.model.chat.Message

@Database(entities = [User::class, Message::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun messageDao(): MessageDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "healthcare_app_db" // Database file name
                )
                // Add migrations here if/when schema changes, for now allow fallback for simplicity if needed during dev
                // .fallbackToDestructiveMigration() // Use only during development if schema changes often
                .build()
                INSTANCE = instance
                // return instance
                instance // Corrected: return instance from synchronized block
            }
        }
    }
}
