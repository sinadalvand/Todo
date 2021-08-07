package ir.roocket.sinadalvand.todo.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.roocket.sinadalvand.todo.repository.UserRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SplashViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val loggedin = MutableLiveData<Boolean>()

    fun loginUser(): MutableLiveData<Boolean> {
        viewModelScope.launch {
            userRepository.validateUser().collect {
                if (it.statusCode == 401 || userRepository.sessionManager.user.value == null)
                    loggedin.postValue(false)
                else
                    loggedin.postValue(true)
            }
        }
        return loggedin
    }

}