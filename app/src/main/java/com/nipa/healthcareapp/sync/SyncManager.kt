package com.nipa.healthcareapp.sync

import android.content.Context
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

class SyncManager(private val context: Context) {
    fun schedulePeriodicSync() {
        val syncWorkRequest = PeriodicWorkRequestBuilder<SyncWorker>(1, TimeUnit.HOURS) // Example: sync every 1 hour
            .build()
        WorkManager.getInstance(context).enqueue(syncWorkRequest)
    }
}
