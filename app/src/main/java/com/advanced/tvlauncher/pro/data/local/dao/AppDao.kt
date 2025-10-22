package com.advanced.tvlauncher.pro.data.local.dao

import androidx.room.*
import com.advanced.tvlauncher.pro.data.local.entity.AppEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {
    
    @Query("SELECT * FROM apps ORDER BY name ASC")
    fun getApps(): Flow<List<AppEntity>>
    
    @Query("SELECT * FROM apps WHERE is_favorite = 1 ORDER BY last_used DESC")
    fun getFavoriteApps(): Flow<List<AppEntity>>
    
    @Query("SELECT * FROM apps WHERE last_used > 0 ORDER BY last_used DESC LIMIT 10")
    fun getRecentApps(): Flow<List<AppEntity>>
    
    @Query("SELECT * FROM apps WHERE category = :category ORDER BY name ASC")
    fun getAppsByCategory(category: String): Flow<List<AppEntity>>
    
    @Query("SELECT * FROM apps WHERE name LIKE :query OR package_name LIKE :query ORDER BY name ASC")
    fun searchApps(query: String): List<AppEntity>
    
    @Query("SELECT * FROM apps WHERE package_name = :packageName")
    suspend fun getApp(packageName: String): AppEntity?
    
    @Query("SELECT DISTINCT category FROM apps ORDER BY category ASC")
    fun getCategories(): Flow<List<String>>
    
    @Query("SELECT * FROM apps ORDER BY usage_count DESC LIMIT :limit")
    fun getMostUsedApps(limit: Int): List<AppEntity>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertApp(app: AppEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(apps: List<AppEntity>)
    
    @Update
    suspend fun updateApp(app: AppEntity)
    
    @Query("UPDATE apps SET is_favorite = :isFavorite WHERE package_name = :packageName")
    suspend fun updateFavorite(packageName: String, isFavorite: Boolean)
    
    @Query("UPDATE apps SET last_used = :timestamp, usage_count = usage_count + 1 WHERE package_name = :packageName")
    suspend fun updateUsage(packageName: String, timestamp: Long)
    
    @Query("DELETE FROM apps WHERE package_name = :packageName")
    suspend fun deleteApp(packageName: String)
    
    @Query("DELETE FROM apps WHERE is_system_app = 1")
    suspend fun deleteSystemApps()
}
