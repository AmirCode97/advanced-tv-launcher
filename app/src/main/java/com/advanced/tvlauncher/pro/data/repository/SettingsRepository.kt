package com.advanced.tvlauncher.pro.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@Singleton
class SettingsRepository @Inject constructor(
    private val context: Context
) {
    
    private val dataStore = context.dataStore
    
    companion object {
        val THEME_KEY = stringPreferencesKey("theme")
        val DYNAMIC_COLOR_KEY = booleanPreferencesKey("dynamic_color")
        val DARK_THEME_KEY = booleanPreferencesKey("dark_theme")
        val ANIMATION_ENABLED_KEY = booleanPreferencesKey("animation_enabled")
        val VOICE_SEARCH_ENABLED_KEY = booleanPreferencesKey("voice_search_enabled")
        val SHOW_SYSTEM_APPS_KEY = booleanPreferencesKey("show_system_apps")
        val SORT_ORDER_KEY = stringPreferencesKey("sort_order")
        val GRID_COLUMNS_KEY = intPreferencesKey("grid_columns")
        val BACKGROUND_IMAGE_KEY = stringPreferencesKey("background_image")
    }
    
    val preferencesFlow: Flow<UserPreference> = dataStore.data
        .map { preferences ->
            UserPreference(
                theme = preferences[THEME_KEY] ?: "dark",
                useDynamicColor = preferences[DYNAMIC_COLOR_KEY] ?: true,
                isDarkTheme = preferences[DARK_THEME_KEY] ?: true,
                animationEnabled = preferences[ANIMATION_ENABLED_KEY] ?: true,
                voiceSearchEnabled = preferences[VOICE_SEARCH_ENABLED_KEY] ?: true,
                showSystemApps = preferences[SHOW_SYSTEM_APPS_KEY] ?: false,
                sortOrder = preferences[SORT_ORDER_KEY] ?: "name",
                gridColumns = preferences[GRID_COLUMNS_KEY] ?: 5,
                backgroundImage = preferences[BACKGROUND_IMAGE_KEY]
            )
        }
    
    suspend fun updateTheme(theme: String) {
        dataStore.edit { preferences ->
            preferences[THEME_KEY] = theme
            preferences[DARK_THEME_KEY] = theme == "dark"
        }
    }
    
    suspend fun toggleDynamicColor(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[DYNAMIC_COLOR_KEY] = enabled
        }
    }
    
    suspend fun toggleAnimation(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[ANIMATION_ENABLED_KEY] = enabled
        }
    }
    
    suspend fun toggleVoiceSearch(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[VOICE_SEARCH_ENABLED_KEY] = enabled
        }
    }
    
    suspend fun toggleSystemApps(show: Boolean) {
        dataStore.edit { preferences ->
            preferences[SHOW_SYSTEM_APPS_KEY] = show
        }
    }
    
    suspend fun updateSortOrder(order: String) {
        dataStore.edit { preferences ->
            preferences[SORT_ORDER_KEY] = order
        }
    }
    
    suspend fun updateGridColumns(columns: Int) {
        dataStore.edit { preferences ->
            preferences[GRID_COLUMNS_KEY] = columns
        }
    }
    
    suspend fun updateBackgroundImage(imagePath: String?) {
        dataStore.edit { preferences ->
            if (imagePath != null) {
                preferences[BACKGROUND_IMAGE_KEY] = imagePath
            } else {
                preferences.remove(BACKGROUND_IMAGE_KEY)
            }
        }
    }
}
