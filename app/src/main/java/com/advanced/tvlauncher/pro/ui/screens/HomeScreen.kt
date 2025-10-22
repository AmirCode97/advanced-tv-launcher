package com.advanced.tvlauncher.pro.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import com.advanced.tvlauncher.pro.ui.components.*
import com.advanced.tvlauncher.pro.ui.theme.AdvancedTVLauncherTheme
import com.advanced.tvlauncher.pro.viewmodel.MainViewModel

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: MainViewModel = hiltViewModel(),
    onAppClick: (AppInfo) -> Unit,
    onSettingsClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val navigationState by viewModel.navigationState.collectAsState()
    
    AdvancedTVLauncherTheme(
        darkTheme = uiState.userPreferences.isDarkTheme,
        dynamicColor = uiState.userPreferences.useDynamicColor
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primaryContainer,
                            MaterialTheme.colorScheme.background
                        )
                    )
                )
        ) {
            when (navigationState) {
                MainViewModel.NavigationState.HOME -> {
                    HomeContent(
                        uiState = uiState,
                        onAppClick = onAppClick,
                        onFavoriteToggle = viewModel::toggleAppFavorite,
                        onCategoryChange = viewModel::changeCategory,
                        onSearch = viewModel::searchApps,
                        onRefresh = viewModel::refreshApps,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                MainViewModel.NavigationState.SETTINGS -> {
                    SettingsScreen(
                        preferences = uiState.userPreferences,
                        onThemeChange = viewModel::changeTheme,
                        onDynamicColorToggle = viewModel::toggleDynamicColor,
                        onAnimationToggle = viewModel::toggleAnimation,
                        onBack = { viewModel.navigateTo(MainViewModel.NavigationState.HOME) },
                        modifier = Modifier.fillMaxSize()
                    )
                }
                MainViewModel.NavigationState.THEMES -> {
                    ThemesScreen(
                        onBack = { viewModel.navigateTo(MainViewModel.NavigationState.HOME) },
                        modifier = Modifier.fillMaxSize()
                    )
                }
                else -> {
                    // Other navigation states
                }
            }
            
            // Navigation Bar
            NavigationBar(
                currentState = navigationState,
                onNavigate = viewModel::navigateTo,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
fun HomeContent(
    uiState: LauncherState,
    onAppClick: (AppInfo) -> Unit,
    onFavoriteToggle: (String) -> Unit,
    onCategoryChange: (AppCategory) -> Unit,
    onSearch: (String) -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        // Search Header
        SearchHeader(
            query = uiState.searchQuery,
            onQueryChange = onSearch,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
        
        // Favorites Section
        if (uiState.favoriteApps.isNotEmpty()) {
            FavoriteAppsRow(
                apps = uiState.favoriteApps,
                onAppClick = onAppClick,
                onFavoriteToggle = onFavoriteToggle,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            )
        }
        
        // Categories Tabs
        CategoryTabs(
            categories = uiState.categories,
            selectedCategory = uiState.selectedCategory,
            onCategorySelect = onCategoryChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )
        
        // Apps Grid
        AppsGrid(
            apps = if (uiState.searchQuery.isNotEmpty()) {
                uiState.searchResults
            } else {
                uiState.apps
            },
            onAppClick = { app ->
                onAppClick(app)
                onFavoriteToggle(app.packageName)
            },
            onFavoriteToggle = onFavoriteToggle,
            isLoading = uiState.isLoading,
            modifier = Modifier
                .weight(1f)
                .padding(16.dp)
        )
    }
}
