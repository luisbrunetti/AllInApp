package com.tawa.allinapp.features.reports.sku

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tawa.allinapp.databinding.RowSkuBinding
import com.tawa.allinapp.models.Sku
import javax.inject.Inject


class SkuAdapter
@Inject constructor(val context: Context) : RecyclerView.Adapter<SkuAdapter.SkuViewHolder>() {

    var collection = ArrayList<Sku>()

    internal var clickListener: (Sku) -> Unit = { _ -> }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        SkuViewHolder.from(parent)

    override fun onBindViewHolder(viewHolder: SkuViewHolder, position: Int) =
        viewHolder.bind(context, collection[position], clickListener)

    override fun getItemCount() = collection.size

    fun setData(item: List<Sku>){
        collection.clear()
        collection = ArrayList(item)
        notifyDataSetChanged()
    }

    class SkuViewHolder(val binding : RowSkuBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(context: Context, item: Sku, clickListener: (Sku) -> Unit) {
            binding.tvProduct.text = item.product
            binding.tvDate.text = item.date
            binding.tvCategory.text = item.category
            binding.tvSubCategory.text = item.subCategory
            binding.tvInventory.text = item.inventory
            binding.tvPrice.text = item.price
        }

        companion object{
            fun from(parent : ViewGroup) : SkuViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = RowSkuBinding.inflate(inflater,parent,false)
                return SkuViewHolder(binding)
            }
        }
    }
}