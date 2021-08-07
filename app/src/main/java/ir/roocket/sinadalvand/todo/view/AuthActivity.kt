package ir.roocket.sinadalvand.todo.view

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import ir.roocket.sinadalvand.todo.R
import ir.roocket.sinadalvand.todo.ToDoApplication
import ir.roocket.sinadalvand.todo.utils.Extension.background
import ir.roocket.sinadalvand.todo.utils.Extension.handleNetwork
import ir.roocket.sinadalvand.todo.viewmodel.AuthViewModel
import kotlinx.coroutines.flow.collect

class AuthActivity : AppCompatActivity() {

    lateinit var model: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        val container = (application as ToDoApplication).container
        model = AuthViewModel(container.userRepo)
    }
}