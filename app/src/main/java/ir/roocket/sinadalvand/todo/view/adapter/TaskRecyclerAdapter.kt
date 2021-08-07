package ir.roocket.sinadalvand.todo.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ir.roocket.sinadalvand.todo.R
import ir.roocket.sinadalvand.todo.data.model.Task
import kotlinx.android.synthetic.main.recycler_item_task.view.*
import kotlin.random.Random

class TaskRecyclerAdapter(
    val listener: TaskCompleteListener? = null,
    val select: (task:Task) -> Unit
) :
    RecyclerView.Adapter<TaskRecyclerAdapter.TaskHolder>() {

    private val tasks = arrayListOf<Task>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskHolder {
        return TaskHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.recycler_item_task, parent, false)
        )
    }

    override fun onBindViewHolder(holder: TaskHolder, position: Int) {
        val task = tasks[position]
        holder.apply {
            setTask(task)
            checkbox.setOnCheckedChangeListener { _, isChecked ->
                task.completed = isChecked
                listener?.onTaskCompleteChange(task)
            }
            this.iv.setOnClickListener { select.invoke(task) }
        }
    }

    override fun getItemCount(): Int = tasks.size


    fun addTask(tasks: Array<Task>) {
        this.tasks.clear()
        this.tasks.addAll(tasks)
        notifyDataSetChanged()
    }


    class TaskHolder(val iv: View) : RecyclerView.ViewHolder(iv) {
        val title = iv.item_recycler_task_title
        val description = iv.item_recycler_task_description
        val checkbox = iv.item_recycler_task_checkbox
        val process = iv.item_recycler_task_indicator

        fun setTask(task: Task) {
            this.title.text = task.title
            this.description.text = task.description
            this.checkbox.isChecked = task.completed
            this.process.setIndicateCounts(task.subTasks.size)
            this.process.setIndicateFillsCount(task.subTasks.filter { it.completed }.size)
        }

    }

    interface TaskCompleteListener {
        fun onTaskCompleteChange(task: Task)
    }


}