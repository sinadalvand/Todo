package ir.roocket.sinadalvand.todo.data.local

import ir.roocket.sinadalvand.todo.data.model.User

/**
 * manage tiny value to hold in local
 */
interface Valutor {

    suspend fun saveUser(user: User)

    suspend fun getUser(): User?

    suspend fun removeUser()

    suspend fun isCloudOn(): Boolean

    suspend fun cloudOn(on: Boolean)
}