package ir.roocket.sinadalvand.todo.repository

import androidx.room.util.StringUtil
import ir.roocket.sinadalvand.todo.data.model.*
import ir.roocket.sinadalvand.todo.data.persistence.dao.TaskDao
import ir.roocket.sinadalvand.todo.data.remote.TodoApiInterface
import ir.roocket.sinadalvand.todo.utils.Extension.background
import ir.roocket.sinadalvand.todo.utils.Extension.handleNetwork
import ir.roocket.sinadalvand.todo.utils.Extension.mutableLiveData
import ir.roocket.sinadalvand.todo.utils.Extension.toJsonString
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepository @Inject constructor(val api: TodoApiInterface, val taskDao: TaskDao) {


    /**
     * get all tasks
     * @return Flow<Array<Task>>
     */
    fun allTask(): Flow<Array<Task>> {
        return flow {
            val tasks = getTasks()
            emit(tasks)
        }.background()
    }

    /**
     * get tasks from database but if understand is empty then try to get them from Network
     * @return Array<Task>
     */
    private suspend fun getTasks(): Array<Task> {
        val tasks = taskDao.getAll()
        if (tasks.isNullOrEmpty()) {
            val onlineTask = api.all()
            onlineTask.data?.forEach {
                it.task?.let { task ->
                    taskDao.insert(task)
                }
            }
        }
        return taskDao.getAll().filter { !it.deleted }.toTypedArray()
    }


    /**
     * get task from local db by using local id
     * @param task_id String
     * @return Flow<Task?>
     */
    fun getTask(task_id: Int): Flow<Task?> {
        return flow {
            val task = taskDao.getTask(task_id)
            emit(task)
        }
    }


    /**
     * add task to local database
     * @param task Task
     * @return Flow<Task>
     */
    fun addTask(task: Task): Flow<Task> {
        return flow {
            if (task.id > 0)
                taskDao.update(task)
            else
                taskDao.insert(task)
            emit(task)
        }.background()
    }


    /**
     * remove task from local database
     * @param id Int
     * @return Flow<Boolean>
     */
    fun removeTask(task: Task): Flow<Boolean> {
        return flow {
            val rawTask = task.apply { deleted = true }
            taskDao.update(rawTask)
            emit(true)
        }.catch { emit(false) }.background()
    }


    fun sendToServer(task: Task): Flow<Response<Task?>> {
        return flow {
            val jsonTask = task.toJsonString().toJsonString()
            var result: TaskActionContainer<TaskContainer>? = null
            result = if (task.sid != null) {
                api.update(task.sid!!, """ {"description":$jsonTask}   """)
            } else {
                api.add(""" {"description":$jsonTask}   """)
            }
            emit(result.data?.task)
        }.background().handleNetwork()
    }

    fun deleteFromServer(task: Task): Flow<Response<Boolean>> {
        return flow {
            if (task.sid != null) {
                api.remove(task.sid!!)
                emit(true)
            } else
                emit(false)
        }.background().handleNetwork()
    }

}