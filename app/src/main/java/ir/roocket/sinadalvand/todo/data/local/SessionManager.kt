package ir.roocket.sinadalvand.todo.data.local

import androidx.lifecycle.MutableLiveData
import ir.roocket.sinadalvand.todo.data.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SessionManager() {

    private var valutor: Valutor? = null

    constructor(valutor: Valutor):this()  {
        this.valutor= valutor
        CoroutineScope(Dispatchers.Unconfined).launch {
            valutor.getUser()?.let {
                token.value = it.token
                user.postValue(it)
            }
        }
    }



    var token = MutableLiveData<String?>()

    var user = MutableLiveData<User?>()


    fun getToken(): String? {
        return token.value
    }


    fun saveUser(user: User) {
        CoroutineScope(Dispatchers.IO).launch {
            user.token?.let {
                token.postValue(user.token!!)
            }
            valutor?.saveUser(user)
            this@SessionManager.user.postValue(user)
        }
    }

    fun removeUser() {
        CoroutineScope(Dispatchers.IO).launch {
            token.postValue(null)
            valutor?.removeUser()
            this@SessionManager.user.postValue(null)
        }
    }


}
