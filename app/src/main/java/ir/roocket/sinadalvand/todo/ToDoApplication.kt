package ir.roocket.sinadalvand.todo

import android.app.Application
import androidx.work.Configuration
import dagger.hilt.android.HiltAndroidApp
import ir.roocket.sinadalvand.todo.data.persistence.TodoDatabase
import ir.roocket.sinadalvand.todo.data.remote.TodoApiInterface
import ir.roocket.sinadalvand.todo.data.workmanager.TaskWordManagerFactory
import ir.roocket.sinadalvand.todo.repository.TaskRepository
import javax.inject.Inject

/**
 *  1- contain all object here
 *  2- implement Configuration.Provider for making instantiate of WorkManager
 */
@HiltAndroidApp
class ToDoApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var api: TodoApiInterface

    @Inject
    lateinit var database: TodoDatabase

    @Inject
    lateinit var taskRepository: TaskRepository

    override fun getWorkManagerConfiguration(): Configuration =
        Configuration.Builder()
            .setWorkerFactory(
                TaskWordManagerFactory(
                    database,
                    api,
                    taskRepository
                )
            )
            .build()
}