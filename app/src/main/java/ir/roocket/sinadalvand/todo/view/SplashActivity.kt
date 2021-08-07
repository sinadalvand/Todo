package ir.roocket.sinadalvand.todo.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import ir.roocket.sinadalvand.todo.R
import ir.roocket.sinadalvand.todo.ToDoApplication
import ir.roocket.sinadalvand.todo.utils.Extension.gone
import ir.roocket.sinadalvand.todo.utils.Extension.invisible
import ir.roocket.sinadalvand.todo.utils.Extension.visible
import ir.roocket.sinadalvand.todo.viewmodel.SplashViewModel
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {

    lateinit var model: SplashViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val container = (application as ToDoApplication).container
        model = SplashViewModel(container.userRepo)


        Glide.with(this).load(R.drawable.img_logo).into(activity_splash_logo)
        activity_splash_title.animateText(getString(R.string.let_s_manage_your_task))


        model.loginUser().observe(this) {
            if (it) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                startActivity(Intent(this, AuthActivity::class.java))
                finish()
            }
        }
    }


}