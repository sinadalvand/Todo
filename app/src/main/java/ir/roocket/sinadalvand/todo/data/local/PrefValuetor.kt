package ir.roocket.sinadalvand.todo.data.local

import android.content.Context
import com.google.gson.GsonBuilder
import dagger.hilt.android.qualifiers.ApplicationContext
import ir.roocket.sinadalvand.todo.ToDoApplication
import ir.roocket.sinadalvand.todo.data.model.User
import javax.inject.Inject


/**
 * save value in sharedpreference
 */
class PrefValuetor @Inject constructor(@ApplicationContext context: Context) : Valutor {

    private val sp = context.getSharedPreferences("app", Context.MODE_PRIVATE)

    private val gson = GsonBuilder().create()

    private val USER_KEY = "USER"
    private val SYNC_KEY = "SYNC"

    override suspend fun saveUser(user: User) {
        sp?.edit()?.putString(USER_KEY, parse2String(user))?.apply()
    }

    override suspend fun getUser(): User? = sp?.getString(USER_KEY, null)?.let { parse2User(it) }

    override suspend fun removeUser() {
        sp?.edit()?.remove(USER_KEY)?.apply()
    }

    override suspend fun isCloudOn(): Boolean {
        return  sp?.getBoolean(SYNC_KEY, true) ?: true
    }

    override suspend fun cloudOn(on: Boolean) {
        sp?.edit()?.putBoolean(SYNC_KEY,on)?.apply()
    }


    private fun parse2String(user: User): String = gson.toJson(user, User::class.java)

    private fun parse2User(user: String): User = gson.fromJson(user, User::class.java)
}