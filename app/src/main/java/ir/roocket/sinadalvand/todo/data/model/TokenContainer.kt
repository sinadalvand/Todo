package ir.roocket.sinadalvand.todo.data.model

import com.google.gson.annotations.SerializedName

data class TokenContainer(
    @SerializedName("user")
    var user: User? = null,
    @SerializedName("token")
    var token: String? = null
)