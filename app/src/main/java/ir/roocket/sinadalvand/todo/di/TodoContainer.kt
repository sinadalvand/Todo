package ir.roocket.sinadalvand.todo.di

import android.content.Context
import android.util.Log
import ir.roocket.sinadalvand.todo.data.local.PrefValuetor
import ir.roocket.sinadalvand.todo.data.local.SessionManager
import ir.roocket.sinadalvand.todo.data.persistence.TodoDatabase
import ir.roocket.sinadalvand.todo.data.remote.TodoApiInterface
import ir.roocket.sinadalvand.todo.repository.TaskRepository
import ir.roocket.sinadalvand.todo.repository.UserRepository
import kotlinx.coroutines.delay
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

class TodoContainer(val context: Context) {

    val valutor = PrefValuetor()

    var session = SessionManager(valutor)

    private val okHttp = OkHttpClient.Builder()
        .addInterceptor(object : Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {
                val original: Request = chain.request()

                val request: Request = original.newBuilder()
                    .header("Authorization", "Bearer ${session.getToken() ?: ""}")
                    .method(original.method, original.body)
                    .build()

                return chain.proceed(request)
            }
        })
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.HEADERS
        })
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit.Builder()
        .client(okHttp)
        .baseUrl("https://api-nodejs-todolist.herokuapp.com/")
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val todoApiCall = retrofit.create(TodoApiInterface::class.java)


    val database = TodoDatabase.create(context)
    val taskDao = database.taskDao()
    val userDao = database.userDao()


    val userRepo = UserRepository(todoApiCall, session)
    val taskRepo = TaskRepository(todoApiCall, taskDao)


}