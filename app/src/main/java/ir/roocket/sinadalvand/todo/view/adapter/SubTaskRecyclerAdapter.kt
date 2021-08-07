package ir.roocket.sinadalvand.todo.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ir.roocket.sinadalvand.todo.R
import ir.roocket.sinadalvand.todo.data.model.SubTask
import kotlinx.android.synthetic.main.recycler_item_subtask.view.*

class SubTaskRecyclerAdapter(val listener: SubTaskClickListener? = null) :
    RecyclerView.Adapter<SubTaskRecyclerAdapter.SubTaskHolder>() {

    private val subTasks = arrayListOf<SubTask>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubTaskHolder {
        return SubTaskHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.recycler_item_subtask, parent, false)
        )
    }

    override fun onBindViewHolder(holder: SubTaskHolder, position: Int) {
        val subTask = this.subTasks[position]
        holder.setTaskHolder(subTask)
        holder.itemView.setOnClickListener {
            holder.changeCheck()
        }
        holder.checkbox.setOnCheckedChangeListener { _, isChecked ->
            changeCheck(subTask,isChecked)
        }
        holder.delete.setOnClickListener {
            listener?.onDeleteSubtask(subTask)
        }
    }

    override fun getItemCount(): Int = subTasks.size

    private fun changeCheck(subTask:SubTask,complete:Boolean){
        subTask.completed = complete
        listener?.onCompleteChangeSubtask(subTask)
    }

    fun addTask(tasks: Array<SubTask>) {
        this.subTasks.clear()
        this.subTasks.addAll(tasks)
        notifyDataSetChanged()
    }


    class SubTaskHolder(iv: View) : RecyclerView.ViewHolder(iv) {
        private val title = iv.item_recycler_subtask_title
        private val description = iv.item_recycler_subtask_description
        val checkbox = iv.item_recycler_subtask_checkbox
        val delete = iv.item_recycler_subtask_delete

        fun setTaskHolder(subtask: SubTask) {
            title.text = subtask.title
            description.text = subtask.description
            checkbox.isChecked = subtask.completed
        }

        fun changeCheck() {
            checkbox.isChecked = !checkbox.isChecked
        }

    }

    interface SubTaskClickListener {
        fun onDeleteSubtask(subtask: SubTask)
        fun onCompleteChangeSubtask(subtask: SubTask)
    }
}