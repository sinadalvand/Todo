package ir.roocket.sinadalvand.todo.data.remote

import ir.roocket.sinadalvand.todo.data.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

/**
 * all request should sent to "https://api-nodejs-todolist.herokuapp.com/" address
 * for documentation refer to "https://documenter.getpostman.com/view/8858534/SW7dX7JG#intro"
 */
interface TodoApiInterface {

    /* <============  user  ============> */
    @Headers("Content-Type: application/json")
    @POST("user/register")
    suspend fun register(
        @Body user: String,
    ): TokenContainer

    @Headers("Content-Type: application/json")
    @POST("user/login")
    suspend fun login(
        @Body user: String,
    ): TokenContainer


    @GET("user/me")
    suspend fun validateMe(): Empty


    @POST("user/me/avatar")
    @Multipart
    suspend fun uploadAvatar(
        @Part image: MultipartBody.Part
    ): Response<Empty>


    /* <============  task  ============> */
    @Headers("Content-Type: application/json")
    @GET("task")
    suspend fun all(): TaskActionContainer<Array<TaskContainer>>

    @Headers("Content-Type: application/json")
    @POST("task")
    suspend fun add(  @Body description: String): TaskActionContainer<TaskContainer>

    @Headers("Content-Type: application/json")
    @PUT("task/{id}")
    suspend fun update(@Path("id") id: String,  @Body description: String,): TaskActionContainer<TaskContainer>

    @Headers("Content-Type: application/json")
    @DELETE("task/{id}")
    suspend fun remove(@Path("id") id: String): Empty
}