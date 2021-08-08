package ir.roocket.sinadalvand.todo.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.roocket.sinadalvand.todo.data.local.SessionManager
import ir.roocket.sinadalvand.todo.data.model.Task
import ir.roocket.sinadalvand.todo.repository.TaskRepository
import ir.roocket.sinadalvand.todo.repository.UserRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val userRepository: UserRepository,
    val taskRepository: TaskRepository
) :
    ViewModel() {

    val tasks = MutableLiveData<Array<Task>>()

    val user = userRepository.sessionManager.user

    fun requestTasks() {
        viewModelScope.launch {
            taskRepository.allTask().collect {
                tasks.postValue(it)
            }
        }
    }

    fun taskUpdate(task: Task) {
        viewModelScope.launch {
            taskRepository.addTask(task).collect {

            }
        }
    }


}