package ir.roocket.sinadalvand.todo.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.roocket.sinadalvand.todo.data.model.SubTask
import ir.roocket.sinadalvand.todo.data.model.Task
import ir.roocket.sinadalvand.todo.data.persistence.dao.TaskDao
import ir.roocket.sinadalvand.todo.repository.TaskRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class TodoViewModel(private val taskRepo: TaskRepository) : ViewModel() {

    val task = MutableLiveData(Task())

    fun getTask(taskId: Int) {
        viewModelScope.launch {
            taskRepo.getTask(taskId).collect {
                if (task.value != null)
                    task.postValue(it)
            }
        }
    }

    fun saveTask(title: String, description: String) {
        viewModelScope.launch {
            taskRepo.addTask(task.value.apply {

            }!!).collect {
                if (task.value != null)
                    task.postValue(it)
            }
        }
    }

    fun deleteTask() {
        viewModelScope.launch {
            taskRepo.removeTask(task.value!!).collect {

            }
        }
    }

    fun addSubTask(subtask: SubTask) {
        task.value?.subTasks?.also {
            it.add(subtask)
            task.postValue(task.value)
        }
    }

    fun removeSubTask(subTask: SubTask) {
        task.value?.let {
            it.subTasks.remove(subTask)
            task.postValue(it)
        }
    }


}