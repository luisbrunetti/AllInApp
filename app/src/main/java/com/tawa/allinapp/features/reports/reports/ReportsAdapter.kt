package com.tawa.allinapp.features.reports.reports

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tawa.allinapp.R
import com.tawa.allinapp.core.extensions.loadFromResource
import com.tawa.allinapp.databinding.RowReportBinding
import com.tawa.allinapp.models.Report
import javax.inject.Inject

class ReportsAdapter
@Inject constructor(val context: Context) : RecyclerView.Adapter<ReportsAdapter.ReportsViewHolder>() {

    var collection = ArrayList<Report>()

    internal var clickListener: (Report) -> Unit = { _ -> }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ReportsViewHolder.from(parent)

    override fun onBindViewHolder(viewHolder: ReportsViewHolder, position: Int) =
        viewHolder.bind(context, collection[position], clickListener)

    override fun getItemCount() = collection.size

    fun setData(reports: List<Report>){
        collection.clear()
        collection = ArrayList(reports)
        notifyDataSetChanged()
    }

    class ReportsViewHolder(val binding : RowReportBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(context: Context, report: Report, clickListener: (Report) -> Unit) {
            when(report.reportName){
                "A"-> binding.ivIconReport.loadFromResource(R.drawable.ic_noinit)
                "B" -> binding.ivIconReport.loadFromResource(R.drawable.ic_noinit)
                else -> binding.ivIconReport.loadFromResource(R.drawable.ic_noinit)
            }
            binding.tvPVName.text = report.reportName
            binding.tvPVSub.text = report.reportName
            binding.clReport.setOnClickListener {
                clickListener(report)
            }
        }

        companion object{
            fun from(parent : ViewGroup) : ReportsViewHolder{
                val inflater = LayoutInflater.from(parent.context)
                val binding = RowReportBinding.inflate(inflater,parent,false)
                return ReportsViewHolder(binding)
            }
        }
    }
}