package ir.roocket.sinadalvand.todo.data.persistence.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ir.roocket.sinadalvand.todo.data.model.UpdateTable

@Dao
interface UpdateDao {

    @Insert
    suspend fun insert(task: UpdateTable)

    @Query("SELECT * FROM update_table ORDER BY id DESC LIMIT 1")
    suspend fun last(): UpdateTable?
}