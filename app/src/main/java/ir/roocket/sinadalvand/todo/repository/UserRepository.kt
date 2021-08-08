package ir.roocket.sinadalvand.todo.repository

import ir.roocket.sinadalvand.todo.data.local.SessionManager
import ir.roocket.sinadalvand.todo.data.local.Valutor
import ir.roocket.sinadalvand.todo.data.model.Response
import ir.roocket.sinadalvand.todo.data.model.TokenContainer
import ir.roocket.sinadalvand.todo.data.model.User
import ir.roocket.sinadalvand.todo.data.remote.TodoApiInterface
import ir.roocket.sinadalvand.todo.utils.Extension.background
import ir.roocket.sinadalvand.todo.utils.Extension.handleNetwork
import ir.roocket.sinadalvand.todo.utils.Extension.toJsonString
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import okhttp3.MultipartBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    val api: TodoApiInterface,
    val sessionManager: SessionManager
) {

    fun login(email: String, password: String): Flow<Response<TokenContainer>> {
        val user = User().apply { this.email = email; this.password = password }
        return flow {
            val result = api.login(user.toJsonString()).also {
                it.user?.token = it.token
            }
            saveUser(result)
            emit(result)
        }.background().handleNetwork()
    }

    fun register(
        name: String,
        password: String,
        email: String,
        age: Int,
        image: File
    ): Flow<Response<TokenContainer>> {
        val user = User(name = name, password = password, email = email, age = age)
        return flow {
            val result = api.register(user.toJsonString()).apply {
                this.user?.token = token
            }
            saveUser(result)
            val imageresult = api.uploadAvatar(getImageForm(image))
            emit(result)
        }.background().handleNetwork()
    }

    fun validateUser(): Flow<Response<Boolean>> = flow {
        api.validateMe()
        emit(true)
    }.background().handleNetwork()

    fun logout(): Flow<Boolean> {
        return flow {
            sessionManager.removeUser()
            emit(true)
        }.background()

    }

    private fun saveUser(data: TokenContainer) {
        if (data.user != null)
            sessionManager.saveUser(data.user!!)
    }

    private fun getImageForm(image: File): MultipartBody.Part {
        val req = image.asRequestBody("image/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("avatar", image.name, req)
    }


}