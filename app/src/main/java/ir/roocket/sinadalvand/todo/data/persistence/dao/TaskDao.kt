package ir.roocket.sinadalvand.todo.data.persistence.dao

import androidx.room.*
import ir.roocket.sinadalvand.todo.data.model.Task

@Dao
interface TaskDao {

    @Query("SELECT * FROM task")
    suspend fun getAll(): List<Task>

    @Query("SELECT * FROM task WHERE id=:id")
    suspend fun getTask(id:Int): Task?

    @Insert
    suspend fun insert(task: Task)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(task: Task)

    @Delete
    suspend fun delete(task: Task)
}