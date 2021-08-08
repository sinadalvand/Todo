package ir.roocket.sinadalvand.todo.view

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import dagger.hilt.android.AndroidEntryPoint
import ir.roocket.sinadalvand.todo.R
import ir.roocket.sinadalvand.todo.ToDoApplication
import ir.roocket.sinadalvand.todo.data.model.SubTask
import ir.roocket.sinadalvand.todo.data.model.Task
import ir.roocket.sinadalvand.todo.repository.TaskRepository
import ir.roocket.sinadalvand.todo.utils.Extension.snack
import ir.roocket.sinadalvand.todo.utils.Extension.visible
import ir.roocket.sinadalvand.todo.view.adapter.SubTaskRecyclerAdapter
import ir.roocket.sinadalvand.todo.viewmodel.TodoViewModel
import kotlinx.android.synthetic.main.activity_todo.*
import javax.inject.Inject

@AndroidEntryPoint
class TodoActivity : AppCompatActivity(), View.OnClickListener,
    SubTaskRecyclerAdapter.SubTaskClickListener {

    private val model: TodoViewModel by viewModels()

    @Inject
    lateinit var taskRepository: TaskRepository

    private val adapter = SubTaskRecyclerAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo)


        val taskId = intent.extras?.getInt("task_id", -1) ?: -1

        // if task is old one and user want to edit then show delete button
        if (taskId >= 0) {
            model.getTask(taskId)
            activity_todo_task_delete.visible()
        }

        activity_todo_task_save.setOnClickListener(this)
        activity_todo_task_delete.setOnClickListener(this)
        activity_todo_subtask_add.setOnClickListener(this)

        activity_todo_recycler_subtask.adapter = adapter


        activity_todo_task_title.doOnTextChanged { text, _, _, _ ->
            model.task.value?.title = text.toString()
        }

        activity_todo_task_description.doOnTextChanged { text, _, _, _ ->
            model.task.value?.description = text.toString()
        }

        model.task.observe(this) {
            setupTask(it)
        }

    }

    private fun setupTask(task: Task) {
        activity_todo_task_title.setText(task.title)
        activity_todo_task_description.setText(task.description)


        adapter.addTask(task.subTasks.toTypedArray())
    }

    override fun onClick(v: View?) {
        when (v) {
            activity_todo_task_save -> {
                saveTask()
            }
            activity_todo_task_delete -> {
                model.deleteTask()
                finish()
            }
            activity_todo_subtask_add -> {
                addSubtask()
            }
        }

    }

    private fun saveTask() {
        val title = activity_todo_task_title.text.toString()
        val description = activity_todo_task_description.text.toString()

        if (title.isNotEmpty()) {
            model.saveTask(title, description)
            finish()
        } else {
            snack(activity_todo_container, getString(R.string.must_not_be_empty))
        }


    }

    private fun addSubtask() {
        val title = activity_todo_subtask_title.text.toString()
        val description = activity_todo_subtask_description.text.toString()

        if (title.isNotEmpty()) {
            val task = SubTask(title).apply {
                this.title = title
                this.description = description
            }
            model.addSubTask(task)

            activity_todo_subtask_title.setText("")
            activity_todo_subtask_description.setText("")
        } else {
            snack(activity_todo_container, getString(R.string.must_not_be_empty))
        }
    }

    override fun onDeleteSubtask(subtask: SubTask) {
        model.removeSubTask(subtask)
    }

    override fun onCompleteChangeSubtask(subtask: SubTask) {

    }


}