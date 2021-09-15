package com.tawa.allinapp.features.reports.geolocation.ui

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.TextView
import com.tawa.allinapp.R


class CheckableSpinnerAdapter<T> internal constructor(
    context: Context,
    headerText: String,
    all_items: ArrayList<SpinnerItem<T>>,
    selected_items: MutableSet<T>
) : BaseAdapter() {
    internal class SpinnerItem<T>(val item: T, val txt: String)
    private val context: Context = context
    val selected_items: MutableSet<T> = selected_items
    private val all_items: List<SpinnerItem<T>> = all_items
    private val headerText: String = headerText
    override fun getCount(): Int {
        return all_items.size + 1
    }

    override fun getItem(position: Int): Any {
        if (position < 1) {
            return -1
        } else {
            return all_items[position - 1]
        }
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        //var convertView: View = convertView
        var convertView = convertView
        val holder: ViewHolder
        if (convertView == null) {
            val layoutInflator = LayoutInflater.from(context)
            convertView = layoutInflator.inflate(R.layout.row_user, parent, false)
            holder = ViewHolder()
            holder.mTextView = convertView.findViewById(R.id.tvUserAdapter)
            holder.mCheckBox = convertView.findViewById(R.id.cbUserAdapter)
            convertView.tag = holder
        } else {
            holder = convertView.tag as CheckableSpinnerAdapter<T>.ViewHolder
        }
        if (position < 1) {
            holder.mCheckBox?.visibility = View.GONE
            holder.mTextView?.text = headerText
        } else {
            val listPos = position - 1
            holder.mCheckBox?.visibility = View.VISIBLE
            holder.mTextView?.text = all_items[listPos].txt
            val item = all_items[listPos].item
            val isSel = selected_items.contains(item)
            //holder.mCheckBox?.setOnCheckedChangeListener(null)
            holder.mCheckBox?.isChecked = isSel
            holder.mCheckBox?.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
                Log.d("asd", "entre")
                if (isChecked) {
                    selected_items.add(item)
                    Log.d("selected_Item", item.toString())
                    Log.d("all list", selected_items.toString())
                } else {
                    selected_items.remove(item)
                }
            })
        /*{
                Log.d("change", it.isC)
                holder.mCheckBox?.toggle()
            }*/
        }
        return convertView
    }

    private inner class ViewHolder {
        var mTextView: TextView? = null
        var mCheckBox: CheckBox? = null
    }

}