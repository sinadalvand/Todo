package ir.roocket.sinadalvand.todo.data.model

import com.google.gson.annotations.SerializedName

data class Response<T>(
    /* response status code*/
    @SerializedName("status")
    var statusCode: Int = 200,

    /* server message */
    @SerializedName("msg")
    var msg: String? = null,

    /* data */
    @SerializedName("data")
    var data: T? = null
)