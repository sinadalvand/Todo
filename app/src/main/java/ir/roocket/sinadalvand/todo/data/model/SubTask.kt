package ir.roocket.sinadalvand.todo.data.model

import com.google.gson.annotations.SerializedName

data class SubTask(
    @SerializedName("title")
    var title: String ,
    @SerializedName("description")
    var description: String= "",
    @SerializedName("completed")
    var completed: Boolean = false,
)