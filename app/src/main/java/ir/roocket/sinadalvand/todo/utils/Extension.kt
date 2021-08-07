package ir.roocket.sinadalvand.todo.utils

import android.content.res.Resources
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveDataScope
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import ir.roocket.sinadalvand.todo.data.model.Response
import ir.roocket.sinadalvand.todo.data.model.SubTask
import ir.roocket.sinadalvand.todo.data.model.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import retrofit2.HttpException
import java.lang.reflect.Type
import java.net.UnknownHostException
import kotlin.experimental.ExperimentalTypeInference
import kotlin.math.roundToInt

object Extension {

    /* <================ String ================> */

    fun <T> String.gsonTo(clas:Class<T>): T {
        return GsonBuilder().create().fromJson(this,clas)
    }

    /* <================ View ================> */

    fun View.visible() {
        visibility = View.VISIBLE
    }

    fun View.invisible() {
        visibility = View.INVISIBLE
    }

    fun View.gone() {
        visibility = View.GONE
    }

    /* <================ flow ================> */
    fun <T> Flow<T>.background(): Flow<T> {
        return this.flowOn(Dispatchers.IO)
    }

    fun <T> Flow<T>.handleNetwork(): Flow<Response<T>> {
        return this.map { data ->
            return@map Response(data = data)
        }.catch { exception ->
            val response = Response<T>().apply {
                when (exception) {
                    is HttpException -> {
                        statusCode = exception.code()
                        msg = exception.message()
                    }
                    else -> {
                        statusCode = -1
                        msg = "Unknown Error happened"
                    }
                }
            }
            emit(response)
        }
    }

    @OptIn(ExperimentalTypeInference::class)
    fun <T> mutableLiveData(
        @BuilderInference block: suspend LiveDataScope<T>.() -> Unit
    ): MutableLiveData<T> = liveData(block = block) as MutableLiveData


    fun Any.toJsonString(): String {
        return GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().toJson(this)
    }

    /* <================ Task ================> */
//    fun Task.parse2SubTask(gson: Gson = GsonBuilder().create()): Task {
//        val listType: Type = object : TypeToken<ArrayList<SubTask>>() {}.type
//        this.subTasks = if (this.subTasks.isNullOrEmpty()) arrayListOf() else gson.fromJson(
//            this.content,
//            listType
//        )
//        return this
//    }

    fun Task.toJsonString(): String {
        return GsonBuilder().create().toJson(this)
    }


    fun Task.parse2String(gson: Gson = GsonBuilder().create()): String {
        return gson.toJson(this.subTasks)
    }

    /* <================ Response ================> */
    fun <T> Response<T>.isOk(): Boolean {
        return statusCode == 200
    }

    /* <================ global ===============> */
    /**
     * convert dp to px size
     **/
    val Int.dpPx: Int get() = (this * Resources.getSystem().displayMetrics.density).roundToInt()

    /**
     * convert pc to dp size
     **/
    val Int.spPx: Int get() = (this * Resources.getSystem().displayMetrics.scaledDensity).roundToInt()


    /* <================ activity ===============> */

    fun AppCompatActivity.snack(view: View, msg: String) {
        Snackbar.make(view, msg, Snackbar.LENGTH_LONG).show()
    }

    /* <================ Fragment ===============> */

    fun Fragment.snack(msg: String) {
        Snackbar.make(requireView(), msg, Snackbar.LENGTH_LONG).show()
    }
}