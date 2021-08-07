package ir.roocket.sinadalvand.todo

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.work.Configuration
import dagger.hilt.android.HiltAndroidApp
import ir.roocket.sinadalvand.todo.data.workmanager.TaskWordManagerFactory
import ir.roocket.sinadalvand.todo.di.TodoContainer

/**
 *  1- contain all object here
 *  2- implement Configuration.Provider for making instantiate of WorkManager
 */
@HiltAndroidApp
class ToDoApplication : Application(),Configuration.Provider {

    companion object {
        // static context for using in constructorless Class
        // it's not standard but works
        var appContext: Context? = null
    }

    // objects container
    lateinit var container: TodoContainer

    override fun onCreate() {
        super.onCreate()
        appContext = this
        container = TodoContainer(this)
    }

    override fun getWorkManagerConfiguration(): Configuration =
        Configuration.Builder()
            .setWorkerFactory(TaskWordManagerFactory(container.database,container.todoApiCall,container.taskRepo))
            .build()
}