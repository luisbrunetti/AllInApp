package com.tawa.allinapp.features.task.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tawa.allinapp.databinding.RowReportBinding
import com.tawa.allinapp.models.TaskReport
import javax.inject.Inject

class TaskAdapter
@Inject constructor(val context:Context) : RecyclerView.Adapter<TaskAdapter.ViewHolderTask>(){

    var listTask = ArrayList<TaskReport>()

    internal var clickListener: (TaskReport) -> Unit = { _ -> }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderTask {
        return ViewHolderTask.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolderTask, position: Int) {
        holder.bind(context, listTask[position], clickListener = clickListener)
    }

    override fun getItemCount(): Int {
        return listTask.size
    }

     class ViewHolderTask(val binding: RowReportBinding) : RecyclerView.ViewHolder(binding.root){

         fun bind(context:Context , report: TaskReport, clickListener: (TaskReport) -> Unit){

         }
        companion object{
            fun from(parent:ViewGroup): ViewHolderTask{
                val inflater = LayoutInflater.from(parent.context)
                val binding = RowReportBinding.inflate(inflater)
                return ViewHolderTask(binding)
            }
        }
    }


}