package ir.roocket.sinadalvand.todo

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

/**
 *  1- contain all object here
 *  2- implement Configuration.Provider for making instantiate of WorkManager
 */
@HiltAndroidApp
class ToDoApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var factory:HiltWorkerFactory

    override fun getWorkManagerConfiguration(): Configuration =
        Configuration.Builder()
            .setWorkerFactory(factory)
            .build()
}