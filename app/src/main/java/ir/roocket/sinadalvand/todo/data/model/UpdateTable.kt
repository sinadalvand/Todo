package ir.roocket.sinadalvand.todo.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import ir.roocket.sinadalvand.todo.data.persistence.convertor.TimeConvertor
import java.util.*

@Entity(tableName = "update_table")
data class UpdateTable(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id:Int?,

    @TypeConverters(TimeConvertor::class)
    var updateDate:Date?
)