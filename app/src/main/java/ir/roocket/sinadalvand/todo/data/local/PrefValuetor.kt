package ir.roocket.sinadalvand.todo.data.local

import android.content.Context
import com.google.gson.GsonBuilder
import ir.roocket.sinadalvand.todo.ToDoApplication
import ir.roocket.sinadalvand.todo.data.model.User


/**
 * save value in sharedpreference
 */
class PrefValuetor : Valutor {

    private val context = ToDoApplication.appContext

    private val sp = context?.getSharedPreferences("app", Context.MODE_PRIVATE)

    private val gson = GsonBuilder().create()

    private val USER_KEY = "USER"

    override suspend fun saveUser(user: User) {
        sp?.edit()?.putString(USER_KEY, parse2String(user))?.apply()
    }

    override suspend fun getUser(): User? = sp?.getString(USER_KEY, null)?.let { parse2User(it) }

    override suspend fun removeUser() {
        sp?.edit()?.remove(USER_KEY)?.apply()
    }

    private fun parse2String(user: User): String = gson.toJson(user, User::class.java)

    private fun parse2User(user: String): User = gson.fromJson(user, User::class.java)
}