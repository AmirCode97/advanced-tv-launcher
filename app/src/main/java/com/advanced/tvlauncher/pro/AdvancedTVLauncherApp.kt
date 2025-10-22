package com.advanced.tvlauncher.pro

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.advanced.tvlauncher.pro.worker.AppUpdateWorker
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidApp
class AdvancedTVLauncherApp : Application() {
    
    @Inject lateinit var workManager: WorkManager
    
    override fun onCreate() {
        super.onCreate()
        setupWorkers()
        setupCrashReporting()
    }
    
    private fun setupWorkers() {
        val appUpdateWork = PeriodicWorkRequestBuilder<AppUpdateWorker>(
            24, TimeUnit.HOURS
        ).build()
        
        workManager.enqueueUniquePeriodicWork(
            "app_update_worker",
            ExistingPeriodicWorkPolicy.KEEP,
            appUpdateWork
        )
    }
    
    private fun setupCrashReporting() {
        // Initialize Firebase Crashlytics and Analytics
    }
}
