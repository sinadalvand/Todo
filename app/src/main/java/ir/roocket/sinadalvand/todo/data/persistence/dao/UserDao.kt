package ir.roocket.sinadalvand.todo.data.persistence.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import ir.roocket.sinadalvand.todo.data.model.User

@Dao
interface UserDao {

    @Query("SELECT * FROM user LIMIT 1")
    suspend fun getUser(): User?

    @Insert
    suspend fun save(users: User)

    @Query("DELETE FROM user")
    suspend fun delete()
}