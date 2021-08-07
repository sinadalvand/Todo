package ir.roocket.sinadalvand.todo.data.workmanager

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.gson.GsonBuilder
import ir.roocket.sinadalvand.todo.data.model.Task
import ir.roocket.sinadalvand.todo.data.model.UpdateTable
import ir.roocket.sinadalvand.todo.data.persistence.TodoDatabase
import ir.roocket.sinadalvand.todo.data.remote.TodoApiInterface
import ir.roocket.sinadalvand.todo.repository.TaskRepository
import ir.roocket.sinadalvand.todo.utils.Extension.parse2String
import kotlinx.coroutines.flow.collect
import java.lang.Exception
import java.util.*

/**
 * workManager class for sync local task with server
 * @property db TodoDatabase
 * @property api TodoApiInterface
 * @property taskDao TaskDao
 * @property updateDao UpdateDao
 * @property gson (com.google.gson.Gson..com.google.gson.Gson?)
 * @constructor
 */
open class TaskWorkManager(
    appContext: Context,
    workerParams: WorkerParameters,
    val taskRepository: TaskRepository,
    val db: TodoDatabase,
    val api: TodoApiInterface,
) :
    CoroutineWorker(appContext, workerParams) {

    val taskDao = db.taskDao()
    val updateDao = db.updateDao()
    val gson = GsonBuilder().create()

    override suspend fun doWork(): Result {
        Log.e("WorkManager", "Started")
        try {
            // get all not checked tasks
            val tasks = getNotUpdated()

            // first delete deleted tasks
            deleteTasks(tasks)

            // add new tasks
            addNewTask(tasks)

            // save last update time
            updateDao.insert(UpdateTable(null, Calendar.getInstance().time))

        } catch (e: Exception) {
            // notify sth was wrong and try to retry it after 10 seconds
            return Result.failure()
        }

        // notify system that everything was going fine
        return Result.success()
    }

    /**
     * get list of all new task that was never touched before or modify after a while
     * @return List<Task>
     */
    private suspend fun getNotUpdated(): List<Task> {
        val lastDate = updateDao.last()?.updateDate ?: Calendar.getInstance().time.apply { this.time = 0 }
        return taskDao.getAll().filter { it.updatedAt?.after(lastDate) ?: true }
    }


    /**
     * add new tasks to server
     * @param tasks List<Task>
     */
    private suspend fun addNewTask(tasks: List<Task>) {
        tasks.filter { !it.deleted }.forEach {
            if(!it.deleted)
            taskRepository.sendToServer(it).collect {response->
                if (response.statusCode == 200) {
                    it.sid = response.data?.sid
                    taskDao.update(it)
                }
            }
        }
    }

    /**
     * delete local task that was deleted and try to sync with server
     * @param tasks List<Task>
     */
    private suspend fun deleteTasks(tasks: List<Task>) {
        tasks.filter { it.deleted }.forEach {
            if (it.sid == null)
                taskDao.delete(it)
            else {
           taskRepository.deleteFromServer(it).collect{response->
                    if (response.statusCode == 200) {
                        taskDao.delete(it)
                    }
                }
            }
        }
    }


}