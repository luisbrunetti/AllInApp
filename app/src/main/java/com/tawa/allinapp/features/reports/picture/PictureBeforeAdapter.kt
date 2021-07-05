package com.tawa.allinapp.features.reports.picture

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tawa.allinapp.R
import com.tawa.allinapp.core.extensions.loadFromResource
import com.tawa.allinapp.core.extensions.loadFromUrl
import com.tawa.allinapp.databinding.RowPictureBinding
import javax.inject.Inject

class PictureBeforeAdapter
@Inject constructor(val context: Context) : RecyclerView.Adapter<PictureBeforeAdapter.PictureBeforeViewHolder>() {

    var collection = ArrayList<Bitmap>()

    internal var clickListener: (Bitmap) -> Unit = { _ -> }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        PictureBeforeViewHolder.from(parent)

    override fun onBindViewHolder(viewHolder: PictureBeforeViewHolder, position: Int) =
        viewHolder.bind(context, collection[position], clickListener)

    override fun getItemCount() = collection.size

    fun setData(pictures: List<Bitmap>){
        collection.clear()
        collection = ArrayList(pictures)
        notifyDataSetChanged()
    }

    class PictureBeforeViewHolder(val binding : RowPictureBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(context: Context, picture: Bitmap, clickListener: (Bitmap) -> Unit) {
            binding.ivPicture.setImageBitmap(picture)
            //binding.ivPicture.loadFromUrl("https://www.redagricola.com/pe/assets/uploads/2020/04/flota-drones-792x591-c-default.jpg")
            binding.llPhoto.setOnClickListener {
                clickListener(picture)
            }
        }

        companion object{
            fun from(parent : ViewGroup) : PictureBeforeViewHolder{
                val inflater = LayoutInflater.from(parent.context)
                val binding = RowPictureBinding.inflate(inflater,parent,false)
                return PictureBeforeViewHolder(binding)
            }
        }
    }
}