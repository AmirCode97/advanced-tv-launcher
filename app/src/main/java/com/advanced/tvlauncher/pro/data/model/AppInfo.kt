package com.advanced.tvlauncher.pro.data.model

import android.graphics.drawable.Drawable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "apps")
data class AppInfo(
    @PrimaryKey
    val packageName: String,
    val name: String,
    val className: String? = null,
    val category: String = "Apps",
    val isFavorite: Boolean = false,
    val isSystemApp: Boolean = false,
    val installTime: Long = 0L,
    val updateTime: Long = 0L,
    val lastUsed: Long = 0L,
    val usageCount: Int = 0,
    val versionName: String? = null,
    val versionCode: Int = 0
) {
    // برای استفاده در UI
    var icon: Drawable? = null
    var isRunning: Boolean = false
}

data class AppCategory(
    val name: String,
    val displayName: String,
    val iconRes: Int? = null,
    val appCount: Int = 0,
    val color: Int? = null
)

data class LauncherState(
    val apps: List<AppInfo> = emptyList(),
    val favoriteApps: List<AppInfo> = emptyList(),
    val recentApps: List<AppInfo> = emptyList(),
    val categories: List<AppCategory> = emptyList(),
    val selectedCategory: AppCategory? = null,
    val searchQuery: String = "",
    val searchResults: List<AppInfo> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null,
    val userPreferences: UserPreference = UserPreference()
)

data class UserPreference(
    val theme: String = "dark",
    val useDynamicColor: Boolean = true,
    val isDarkTheme: Boolean = true,
    val animationEnabled: Boolean = true,
    voiceSearchEnabled: Boolean = true,
    val showSystemApps: Boolean = false,
    val sortOrder: String = "name",
    val gridColumns: Int = 5,
    val backgroundImage: String? = null
)
