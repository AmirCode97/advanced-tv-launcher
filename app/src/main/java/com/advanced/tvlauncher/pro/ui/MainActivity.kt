package com.advanced.tvlauncher.pro.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.tv.material3.ExperimentalTvMaterial3Api
import com.advanced.tvlauncher.pro.ui.screens.HomeScreen
import com.advanced.tvlauncher.pro.ui.theme.AdvancedTVLauncherTheme
import com.advanced.tvlauncher.pro.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    
    @OptIn(ExperimentalTvMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContent {
            AdvancedTVLauncherTheme {
                val viewModel: MainViewModel = hiltViewModel()
                
                HomeScreen(
                    viewModel = viewModel,
                    onAppClick = { appInfo ->
                        // Launch app
                        launchApp(appInfo)
                    },
                    onSettingsClick = {
                        // Open settings
                        openSettings()
                    }
                )
            }
        }
    }
    
    private fun launchApp(appInfo: AppInfo) {
        try {
            val intent = packageManager.getLaunchIntentForPackage(appInfo.packageName)
            intent?.let {
                startActivity(it)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    private fun openSettings() {
        // Implement settings navigation
    }
}
