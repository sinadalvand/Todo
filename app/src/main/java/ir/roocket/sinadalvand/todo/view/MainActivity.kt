package ir.roocket.sinadalvand.todo.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.work.*
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import dagger.hilt.android.AndroidEntryPoint
import ir.roocket.sinadalvand.todo.R
import ir.roocket.sinadalvand.todo.ToDoApplication
import ir.roocket.sinadalvand.todo.data.local.PrefValuetor
import ir.roocket.sinadalvand.todo.data.local.Valutor
import ir.roocket.sinadalvand.todo.data.model.SubTask
import ir.roocket.sinadalvand.todo.data.model.Task
import ir.roocket.sinadalvand.todo.data.model.User
import ir.roocket.sinadalvand.todo.data.workmanager.TaskWorkManager
import ir.roocket.sinadalvand.todo.repository.TaskRepository
import ir.roocket.sinadalvand.todo.repository.UserRepository
import ir.roocket.sinadalvand.todo.utils.Extension.invisible
import ir.roocket.sinadalvand.todo.utils.Extension.visible
import ir.roocket.sinadalvand.todo.view.adapter.TaskRecyclerAdapter
import ir.roocket.sinadalvand.todo.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), View.OnClickListener,
    TaskRecyclerAdapter.TaskCompleteListener {

    private val model: MainViewModel by viewModels()

    @Inject
    lateinit var valutor: Valutor

    @Inject
    lateinit var taskRepo: TaskRepository

    @Inject
    lateinit var userRepository: UserRepository

    private val recyclerAdapter = TaskRecyclerAdapter(this) {
        gotoTaskDetails(it)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        textSwitcher()

        activity_main_recycler.adapter = recyclerAdapter
        activity_main_task_new.setOnClickListener(this)
        activity_main_setting.setOnClickListener(this)

        model.user.observe(this) {
            it?.let {
                setupUser(it)
            }
        }

        model.tasks.observe(this) {
            addTasks(it)
            if (it.isEmpty())
                activity_main_empty.visible()
            else
                activity_main_empty.invisible()
        }

    }

    /* setup user to show details on top of screen*/
    private fun setupUser(user: User) {
        Glide.with(this).load(user.imageUrl).apply(RequestOptions().error(R.mipmap.ic_launcher))
            .into(activity_main_image)
        activity_main_user_name.text = getString(R.string.hi_user, user.name)
    }

    private fun addTasks(tasks: Array<Task>) {
        recyclerAdapter.addTask(tasks)
    }

    /* show task details when click on it*/
    private fun gotoTaskDetails(task: Task) {
        startActivity(Intent(this, TodoActivity::class.java).apply { putExtra("task_id", task.id) })
    }

    override fun onResume() {
        super.onResume()

        /* config cloud sync*/
        configWorkManager(valutor)

        /* ger fresh task again */
        model.requestTasks()
    }

    /* counter to show text switcher in top of screen*/
    private fun textSwitcher(): Job {
        return lifecycleScope.launchWhenCreated {
            val dialogs = arrayOf(
                getString(R.string.dialog1),
                getString(R.string.dialog4),
                getString(R.string.dialog2),
                getString(R.string.dialog3)
            )
            var counter = 1
            while (true) {
                delay(4000)
                counter++
                activity_main_talk.animateText(dialogs[counter % dialogs.size])
            }
        }
    }

    /**
     * set work manager to be sure every hour will sync your data
     */
    private fun configWorkManager(valutor: Valutor) {
        val workmanager = WorkManager.getInstance(this)
        lifecycleScope.launchWhenResumed {
            if (valutor.isCloudOn()) {
                val uploadWorkRequest: PeriodicWorkRequest =
                    PeriodicWorkRequestBuilder<TaskWorkManager>(2, TimeUnit.HOURS)
                        .setConstraints(
                            // sensitive to network connection
                            Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED)
                                .build()
                        ).build()


                workmanager.enqueueUniquePeriodicWork(
                    "syncer",
                    ExistingPeriodicWorkPolicy.KEEP,
                    uploadWorkRequest
                )
            } else
                workmanager.cancelUniqueWork("syncer")
        }
    }

    override fun onClick(v: View?) {
        when (v) {
            activity_main_task_new -> {
                startActivity(Intent(this, TodoActivity::class.java))
            }
            activity_main_setting -> {
                startActivity(Intent(this, SettingsActivity::class.java))
            }
        }

    }

    override fun onTaskCompleteChange(task: Task) {
        model.taskUpdate(task)
    }


}