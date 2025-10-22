package com.advanced.tvlauncher.pro.data.repository

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import com.advanced.tvlauncher.pro.data.local.AppDatabase
import com.advanced.tvlauncher.pro.data.local.entity.AppEntity
import com.advanced.tvlauncher.pro.data.model.AppInfo
import com.advanced.tvlauncher.pro.data.model.AppCategory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppRepository @Inject constructor(
    private val database: AppDatabase,
    private val context: Context
) {
    
    private val appDao = database.appDao()
    private val packageManager = context.packageManager
    
    fun getApps(): Flow<List<AppInfo>> {
        return appDao.getApps().map { entities ->
            entities.map { it.toAppInfo().loadIcon(packageManager) }
        }
    }
    
    fun getFavoriteApps(): Flow<List<AppInfo>> {
        return appDao.getFavoriteApps().map { entities ->
            entities.map { it.toAppInfo().loadIcon(packageManager) }
        }
    }
    
    fun getRecentApps(): Flow<List<AppInfo>> {
        return appDao.getRecentApps().map { entities ->
            entities.map { it.toAppInfo().loadIcon(packageManager) }
        }
    }
    
    fun getCategories(): Flow<List<AppCategory>> {
        return appDao.getCategories().map { categories ->
            categories.map { categoryName ->
                AppCategory(
                    name = categoryName,
                    displayName = categoryName.replaceFirstChar { 
                        if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() 
                    },
                    appCount = getAppCountByCategory(categoryName)
                )
            }
        }
    }
    
    suspend fun toggleFavorite(packageName: String) {
        val currentApp = appDao.getApp(packageName)
        currentApp?.let {
            appDao.updateFavorite(packageName, !it.isFavorite)
        }
    }
    
    suspend fun updateAppUsage(packageName: String) {
        appDao.updateUsage(packageName, System.currentTimeMillis())
    }
    
    suspend fun searchApps(query: String): List<AppInfo> {
        return if (query.isNotEmpty()) {
            appDao.searchApps("%$query%").map { 
                it.toAppInfo().loadIcon(packageManager) 
            }
        } else {
            emptyList()
        }
    }
    
    suspend fun refreshInstalledApps() {
        val installedApps = getInstalledAppsFromSystem()
        appDao.insertAll(installedApps.map { it.toAppEntity() })
    }
    
    private fun getInstalledAppsFromSystem(): List<AppInfo> {
        val apps = mutableListOf<AppInfo>()
        val intent = android.content.Intent(android.content.Intent.ACTION_MAIN, null).apply {
            addCategory(android.content.Intent.CATEGORY_LAUNCHER)
            addCategory(android.content.Intent.CATEGORY_LEANBACK_LAUNCHER)
        }
        
        val resolvedInfos = packageManager.queryIntentActivities(intent, PackageManager.MATCH_ALL)
        
        resolvedInfos.forEach { resolveInfo ->
            val activityInfo = resolveInfo.activityInfo
            if (activityInfo != null) {
                val appInfo = AppInfo(
                    packageName = activityInfo.packageName,
                    name = activityInfo.loadLabel(packageManager).toString(),
                    className = activityInfo.name,
                    category = getAppCategory(activityInfo.packageName, activityInfo.name),
                    isSystemApp = isSystemApp(activityInfo.applicationInfo),
                    installTime = getInstallTime(activityInfo.packageName),
                    updateTime = getUpdateTime(activityInfo.packageName),
                    versionName = getVersionName(activityInfo.packageName),
                    versionCode = getVersionCode(activityInfo.packageName)
                ).loadIcon(packageManager)
                
                apps.add(appInfo)
            }
        }
        
        return apps.sortedBy { it.name }
    }
    
    private fun AppInfo.loadIcon(pm: PackageManager): AppInfo {
        return try {
            val icon = pm.getApplicationIcon(this.packageName)
            this.copy().apply { this.icon = icon }
        } catch (e: Exception) {
            this
        }
    }
    
    private fun AppEntity.toAppInfo(): AppInfo {
        return AppInfo(
            packageName = packageName,
            name = name,
            className = className,
            category = category,
            isFavorite = isFavorite,
            isSystemApp = isSystemApp,
            installTime = installTime,
            updateTime = updateTime,
            lastUsed = lastUsed,
            usageCount = usageCount,
            versionName = versionName,
            versionCode = versionCode
        )
    }
    
    private fun AppInfo.toAppEntity(): AppEntity {
        return AppEntity(
            packageName = packageName,
            name = name,
            className = className,
            category = category,
            isFavorite = isFavorite,
            isSystemApp = isSystemApp,
            installTime = installTime,
            updateTime = updateTime,
            lastUsed = lastUsed,
            usageCount = usageCount,
            versionName = versionName,
            versionCode = versionCode
        )
    }
    
    private fun getAppCategory(packageName: String, className: String?): String {
        return when {
            packageName.contains("game") || className?.contains("game") == true -> "Games"
            packageName.contains("video") || packageName.contains("tv") -> "Video"
            packageName.contains("music") || packageName.contains("audio") -> "Music"
            packageName.contains("browser") -> "Browser"
            packageName.contains("settings") -> "Settings"
            else -> "Apps"
        }
    }
    
    private fun isSystemApp(appInfo: android.content.pm.ApplicationInfo): Boolean {
        return (appInfo.flags and android.content.pm.ApplicationInfo.FLAG_SYSTEM) != 0
    }
    
    private fun getInstallTime(packageName: String): Long {
        return try {
            val packageInfo = packageManager.getPackageInfo(packageName, 0)
            packageInfo.firstInstallTime
        } catch (e: Exception) {
            System.currentTimeMillis()
        }
    }
    
    private fun getUpdateTime(packageName: String): Long {
        return try {
            val packageInfo = packageManager.getPackageInfo(packageName, 0)
            packageInfo.lastUpdateTime
        } catch (e: Exception) {
            System.currentTimeMillis()
        }
    }
    
    private fun getVersionName(packageName: String): String? {
        return try {
            val packageInfo = packageManager.getPackageInfo(packageName, 0)
            packageInfo.versionName
        } catch (e: Exception) {
            null
        }
    }
    
    private fun getVersionCode(packageName: String): Int {
        return try {
            val packageInfo = packageManager.getPackageInfo(packageName, 0)
            packageInfo.versionCode
        } catch (e: Exception) {
            0
        }
    }
    
    private suspend fun getAppCountByCategory(category: String): Int {
        return appDao.getAppsByCategory(category).map { it.size }.first()
    }
}
