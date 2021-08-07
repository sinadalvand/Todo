package ir.roocket.sinadalvand.todo.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import ir.roocket.sinadalvand.todo.R
import ir.roocket.sinadalvand.todo.ToDoApplication
import ir.roocket.sinadalvand.todo.data.local.PrefValuetor
import kotlinx.android.synthetic.main.activity_settings.*
import javax.inject.Inject

@AndroidEntryPoint
class SettingsActivity : AppCompatActivity() {

    @Inject
    lateinit var prefValutor:PrefValuetor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        checkCloudSync(prefValutor)


        activity_setting_switch.setOnCheckedChangeListener { _, isChecked ->
            lifecycleScope.launchWhenResumed {
                prefValutor.cloudOn(isChecked)
            }
        }
    }


    private fun checkCloudSync(prefValutor:PrefValuetor){
        lifecycleScope.launchWhenResumed {
            activity_setting_switch.isChecked = prefValutor.isCloudOn()
        }
    }


}