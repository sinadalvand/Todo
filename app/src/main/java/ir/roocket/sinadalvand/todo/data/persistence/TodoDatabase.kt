package ir.roocket.sinadalvand.todo.data.persistence

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ir.roocket.sinadalvand.todo.data.model.Task
import ir.roocket.sinadalvand.todo.data.model.UpdateTable
import ir.roocket.sinadalvand.todo.data.model.User
import ir.roocket.sinadalvand.todo.data.persistence.convertor.SubTaskConvertor
import ir.roocket.sinadalvand.todo.data.persistence.convertor.TimeConvertor
import ir.roocket.sinadalvand.todo.data.persistence.dao.TaskDao
import ir.roocket.sinadalvand.todo.data.persistence.dao.UpdateDao
import ir.roocket.sinadalvand.todo.data.persistence.dao.UserDao

@Database(entities = [Task::class, User::class, UpdateTable::class], version = 1)
@TypeConverters(TimeConvertor::class, SubTaskConvertor::class)
abstract class TodoDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao

    abstract fun updateDao(): UpdateDao

    abstract fun userDao(): UserDao

    companion object {
        fun create(context: Context): TodoDatabase {
            return Room.databaseBuilder(context, TodoDatabase::class.java, "task_db").build()
        }
    }
}