package ir.roocket.sinadalvand.todo.data.persistence.convertor

import androidx.room.TypeConverter
import java.util.*

/**
 * simple "updated_at" convertor for get last update time and set new date for update
 */
class TimeConvertor {

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long {
        return Calendar.getInstance().time.time
    }

}