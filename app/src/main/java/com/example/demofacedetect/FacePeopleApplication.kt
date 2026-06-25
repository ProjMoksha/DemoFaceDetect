package com.example.demofacedetect

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

/**
 * Application entry point that bootstraps Hilt for the whole offline face-recognition app.
 *
 * The application also supplies WorkManager's configuration so @HiltWorker classes are
 * constructed through Hilt instead of WorkManager's reflection fallback.
 */
@HiltAndroidApp
class FacePeopleApplication : Application(), Configuration.Provider {
    @Inject lateinit var workerFactory: HiltWorkerFactory

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
}
