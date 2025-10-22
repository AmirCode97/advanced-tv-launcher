package com.advanced.tvlauncher.pro.di

import android.content.Context
import androidx.room.Room
import com.advanced.tvlauncher.pro.data.local.AppDatabase
import com.advanced.tvlauncher.pro.data.repository.AppRepository
import com.advanced.tvlauncher.pro.data.repository.SettingsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }
    
    @Provides
    @Singleton
    fun provideAppRepository(
        database: AppDatabase,
        @ApplicationContext context: Context
    ): AppRepository {
        return AppRepository(database, context)
    }
    
    @Provides
    @Singleton
    fun provideSettingsRepository(@ApplicationContext context: Context): SettingsRepository {
        return SettingsRepository(context)
    }
}
