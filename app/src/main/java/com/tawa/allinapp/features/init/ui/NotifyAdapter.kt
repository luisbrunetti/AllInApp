package com.tawa.allinapp.features.init.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.tawa.allinapp.R
import com.tawa.allinapp.core.extensions.loadFromResource
import com.tawa.allinapp.databinding.RowNotifyBinding
import com.tawa.allinapp.databinding.RowReportBinding
import com.tawa.allinapp.models.Notify
import com.tawa.allinapp.models.Report
import javax.inject.Inject

class NotifyAdapter
@Inject constructor(val context: Context) : RecyclerView.Adapter<NotifyAdapter.NotifyViewHolder>() {

    var collection = ArrayList<Notify>()

    internal var clickListener: (Notify) -> Unit = { _ -> }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        NotifyViewHolder.from(parent)

    override fun onBindViewHolder(viewHolder: NotifyViewHolder, position: Int) =
        viewHolder.bind(context, collection[position], clickListener)

    override fun getItemCount() = collection.size

    fun setData(notify: List<Notify>){
        collection.clear()
        collection = ArrayList(notify)
        notifyDataSetChanged()
    }

    class NotifyViewHolder(val binding : RowNotifyBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(context: Context, notify: Notify, clickListener: (Notify) -> Unit) {
            binding.txInit.text = notify.init
            binding.txContent.text = notify.text
            binding.vNotify.setOnClickListener {
                clickListener(notify)
            }
        }

        companion object{
            fun from(parent : ViewGroup) : NotifyViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = RowNotifyBinding.inflate(inflater,parent,false)
                return NotifyViewHolder(binding)
            }
        }
    }
}