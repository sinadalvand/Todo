package ir.roocket.sinadalvand.todo.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.roocket.sinadalvand.todo.repository.UserRepository
import ir.roocket.sinadalvand.todo.utils.Extension.isOk
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    val loginResult = MutableLiveData<Boolean>()

    var email: String = ""

    var password: String = ""

    var name: String = ""

    var age: Int = 21

    var picture: File? = null


    fun register() {
        viewModelScope.launch {
            userRepository.register(name, password, email, age, picture!!)
                .collect {
                    if (it.isOk() && it.data?.token != null) {
                        loginResult.postValue(true)
                    } else {
                        loginResult.postValue(false)
                    }
                }
        }

    }

    fun login() {
        viewModelScope.launch {
            userRepository.login(email, password)
                .collect {
                    if (it.isOk() && it.data?.token != null) {
                        loginResult.postValue(true)
                    } else {
                        loginResult.postValue(false)
                    }
                }
        }
    }
}