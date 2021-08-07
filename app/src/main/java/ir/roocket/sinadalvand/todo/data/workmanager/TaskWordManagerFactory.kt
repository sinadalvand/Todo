package ir.roocket.sinadalvand.todo.data.workmanager

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import ir.roocket.sinadalvand.todo.data.persistence.TodoDatabase
import ir.roocket.sinadalvand.todo.data.remote.TodoApiInterface
import ir.roocket.sinadalvand.todo.repository.TaskRepository

/**
 * factory class for use workManager with custom dependency
 * @property db TodoDatabase
 * @property api TodoApiInterface
 * @constructor
 */
class TaskWordManagerFactory(val db: TodoDatabase, val api: TodoApiInterface,val taskRepository: TaskRepository) : WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker {
        return TaskWorkManager(appContext,workerParameters,taskRepository,db,api)
    }
}