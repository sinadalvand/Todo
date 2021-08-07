package ir.roocket.sinadalvand.todo.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import ir.roocket.sinadalvand.todo.data.persistence.convertor.TimeConvertor
import ir.roocket.sinadalvand.todo.utils.Extension.gsonTo
import java.util.*

@Entity(tableName = "task")
data class Task(

    // task id make it unique in local database
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    // server id
    var sid: String? = null,

    // task title
    var title: String = "",

    // task title
    var description: String = "",

    // save subtask in database
    var subTasks: ArrayList<SubTask> = arrayListOf(),

    // task have been completed
    var completed: Boolean = false,

    // value for hold deleted data as temporary data till workManager delete it
    var deleted: Boolean = false,

    // last update time in database for knowing last update time
    @Expose
    @TypeConverters(TimeConvertor::class)
    var updatedAt: Date? = null,
)


data class TaskContainer(
    @SerializedName("_id")
    private var sid: String? = null,
    @SerializedName("description")
    private var description: String? = null,
) {
    val task: Task?
        get() = description?.gsonTo(Task::class.java).apply { this?.sid = sid }
}


data class TaskActionContainer<T>(
    @SerializedName("success")
     var success: Boolean = false,
    @SerializedName("count")
     var count: Int = 0,
    @SerializedName("data")
     var data: T?,
)