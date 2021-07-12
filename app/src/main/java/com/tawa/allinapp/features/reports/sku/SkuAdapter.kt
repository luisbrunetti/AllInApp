package com.tawa.allinapp.features.reports.sku

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tawa.allinapp.databinding.RowSkuBinding
import com.tawa.allinapp.databinding.RowSkuHeaderBinding
import com.tawa.allinapp.models.Sku
import javax.inject.Inject


class SkuAdapter
@Inject constructor(val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val HEADER = 1
    var collection = ArrayList<Sku>()

    internal var clickListener: (Sku) -> Unit = { _ -> }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        when(viewType){
            HEADER -> SkuViewHolderHeader.from(parent)
            else -> SkuViewHolder.from(parent)
        }

    override fun getItemViewType(position: Int) = if(position==0) HEADER else 0

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
        when(holder.itemViewType){
            HEADER -> (holder as SkuViewHolderHeader).bind(context, collection[position], clickListener)
            else -> (holder as SkuViewHolder).bind(context, collection[position], clickListener)
        }

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

    class SkuViewHolderHeader(val binding : RowSkuHeaderBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(context: Context, item: Sku, clickListener: (Sku) -> Unit) {
            binding.tvProduct.text = "Producto"
            binding.tvDate.text = "Fecha Data"
            binding.tvCategory.text = "Categoria"
            binding.tvSubCategory.text = "Subcategoría"
            binding.tvInventory.text = "Inventario"
            binding.tvPrice.text = "Precio"
        }

        companion object{
            fun from(parent : ViewGroup) : SkuViewHolderHeader {
                val inflater = LayoutInflater.from(parent.context)
                val binding = RowSkuHeaderBinding.inflate(inflater,parent,false)
                return SkuViewHolderHeader(binding)
            }
        }
    }
}