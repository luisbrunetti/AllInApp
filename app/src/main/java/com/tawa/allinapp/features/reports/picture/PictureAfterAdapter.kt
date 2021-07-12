package com.tawa.allinapp.features.reports.picture

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tawa.allinapp.core.extensions.loadFromUrl
import com.tawa.allinapp.databinding.RowPictureBinding
import javax.inject.Inject

class PictureAfterAdapter
@Inject constructor(val context: Context) : RecyclerView.Adapter<PictureAfterAdapter.PictureAfterViewHolder>() {

    var collection = ArrayList<String?>()

    internal var clickListener: (String) -> Unit = { _ -> }
    internal var deleteListener: (String) -> Unit = { _ -> }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        PictureAfterViewHolder.from(parent)

    override fun onBindViewHolder(viewHolder: PictureAfterViewHolder, position: Int) =
        viewHolder.bind(context, collection[position]?:"", clickListener,deleteListener)

    override fun getItemCount() = collection.size

    fun setData(pictures: List<String>){
        collection.clear()
        collection = ArrayList(pictures)
        notifyDataSetChanged()
    }

    class PictureAfterViewHolder(val binding : RowPictureBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(context: Context, picture: String, clickListener: (String) -> Unit, deleteListener: (String) -> Unit) {
            binding.ivPicture.loadFromUrl(picture)
            binding.ivPicture.setOnClickListener {
                clickListener(picture)
            }
            binding.ivClose.setOnClickListener {
                deleteListener(picture)
            }
        }

        companion object{
            fun from(parent : ViewGroup) : PictureAfterViewHolder{
                val inflater = LayoutInflater.from(parent.context)
                val binding = RowPictureBinding.inflate(inflater,parent,false)
                return PictureAfterViewHolder(binding)
            }
        }
    }
}