package ir.roocket.sinadalvand.todo.data.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.File

@Entity(tableName = "user")
data class User(

    /* user unique id*/

    @PrimaryKey
    @SerializedName("_id")
    var id: String = "",

    /* user name that show in user profile*/
    @SerializedName("name")
    @Expose
    var name: String? = null,

    /* user email use for login */
    @SerializedName("email")
    @Expose
    var email: String? = null,

    /* user age */
    @SerializedName("age")
    @Expose
    var age: Int? = null,

    /* token that use for validation*/
    @SerializedName("token")
    var token: String? = null,

    /* user password for register part*/
    @SerializedName("password")
    @Expose
    var password: String? = null,

    ) {
    /* user Url if registered*/
    val imageUrl: String
        get() = "https://api-nodejs-todolist.herokuapp.com/user/${id ?: ""}/avatar"

}