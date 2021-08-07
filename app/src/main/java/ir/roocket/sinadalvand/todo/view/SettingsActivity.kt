package ir.roocket.sinadalvand.todo.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import ir.roocket.sinadalvand.todo.R
import ir.roocket.sinadalvand.todo.ToDoApplication
import ir.roocket.sinadalvand.todo.data.local.PrefValuetor
import ir.roocket.sinadalvand.todo.data.local.Valutor
import kotlinx.android.synthetic.main.activity_settings.*
import javax.inject.Inject

@AndroidEntryPoint
class SettingsActivity : AppCompatActivity() {

    @Inject
    lateinit var valuetor:Valutor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        checkCloudSync(valuetor)


        activity_setting_switch.setOnCheckedChangeListener { _, isChecked ->
            lifecycleScope.launchWhenResumed {
                valuetor.cloudOn(isChecked)
            }
        }
    }


    private fun checkCloudSync(valuetor:Valutor){
        lifecycleScope.launchWhenResumed {
            activity_setting_switch.isChecked = valuetor.isCloudOn()
        }
    }


}