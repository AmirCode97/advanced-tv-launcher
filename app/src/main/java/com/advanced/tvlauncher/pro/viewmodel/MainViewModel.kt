package com.advanced.tvlauncher.pro.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.advanced.tvlauncher.pro.data.model.*
import com.advanced.tvlauncher.pro.data.repository.AppRepository
import com.advanced.tvlauncher.pro.data.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val appRepository: AppRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(LauncherState())
    val uiState: StateFlow<LauncherState> = _uiState.asStateFlow()
    
    private val _navigationState = MutableStateFlow(NavigationState.HOME)
    val navigationState: StateFlow<NavigationState> = _navigationState.asStateFlow()
    
    init {
        loadInitialData()
        observePreferences()
    }
    
    private fun loadInitialData() {
        viewModelScope.launch {
            // Load apps and categories in parallel
            val appsFlow = appRepository.getApps()
            val favoritesFlow = appRepository.getFavoriteApps()
            val recentFlow = appRepository.getRecentApps()
            val categoriesFlow = appRepository.getCategories()
            
            combine(
                appsFlow,
                favoritesFlow,
                recentFlow,
                categoriesFlow,
                settingsRepository.preferencesFlow
            ) { apps, favorites, recent, categories, preferences ->
                _uiState.update { currentState ->
                    currentState.copy(
                        apps = apps,
                        favoriteApps = favorites,
                        recentApps = recent,
                        categories = categories,
                        userPreferences = preferences,
                        isLoading = false
                    )
                }
            }.collect()
        }
    }
    
    private fun observePreferences() {
        viewModelScope.launch {
            settingsRepository.preferencesFlow.collect { preferences ->
                _uiState.update { it.copy(userPreferences = preferences) }
            }
        }
    }
    
    fun toggleAppFavorite(packageName: String) {
        viewModelScope.launch {
            appRepository.toggleFavorite(packageName)
        }
    }
    
    fun updateAppUsage(packageName: String) {
        viewModelScope.launch {
            appRepository.updateAppUsage(packageName)
        }
    }
    
    fun searchApps(query: String) {
        viewModelScope.launch {
            val results = appRepository.searchApps(query)
            _uiState.update { it.copy(searchResults = results, searchQuery = query) }
        }
    }
    
    fun changeCategory(category: AppCategory) {
        _uiState.update { it.copy(selectedCategory = category) }
    }
    
    fun changeTheme(theme: String) {
        viewModelScope.launch {
            settingsRepository.updateTheme(theme)
        }
    }
    
    fun toggleDynamicColor(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.toggleDynamicColor(enabled)
        }
    }
    
    fun toggleAnimation(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.toggleAnimation(enabled)
        }
    }
    
    fun navigateTo(state: NavigationState) {
        _navigationState.value = state
    }
    
    fun refreshApps() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            appRepository.refreshInstalledApps()
        }
    }
    
    sealed class NavigationState {
        object HOME : NavigationState()
        object APPS : NavigationState()
        object GAMES : NavigationState()
        object SETTINGS : NavigationState()
        object THEMES : NavigationState()
        object SEARCH : NavigationState()
    }
}
