package com.advanced.tvlauncher.pro.data.local

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.advanced.tvlauncher.pro.data.local.dao.AppDao
import com.advanced.tvlauncher.pro.data.local.entity.AppEntity

@Database(
    entities = [AppEntity::class],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    
    abstract fun appDao(): AppDao
    
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        
        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "tv_launcher.db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
