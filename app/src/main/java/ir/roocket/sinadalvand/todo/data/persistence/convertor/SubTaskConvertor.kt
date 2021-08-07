package ir.roocket.sinadalvand.todo.data.persistence.convertor

import androidx.room.TypeConverter
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import ir.roocket.sinadalvand.todo.data.model.SubTask
import java.lang.reflect.Type


/**
 * convert array task into json string and retrieve
 */
class SubTaskConvertor {

    private val gson = GsonBuilder().create()

    @TypeConverter
    fun fromsubTask2String(taksk: ArrayList<SubTask>): String? {
        return gson.toJson(taksk)
    }

    @TypeConverter
    fun fromString2subTasks(tasks: String?): ArrayList<SubTask>? {
        val listType: Type = object : TypeToken<ArrayList<SubTask>>() {}.type
        return if (tasks.isNullOrEmpty()) arrayListOf() else gson.fromJson(tasks, listType)
    }

}