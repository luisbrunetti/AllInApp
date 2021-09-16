package com.tawa.allinapp.features.reports.geolocation.ui

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.MutableLiveData
import com.tawa.allinapp.R
import com.tawa.allinapp.models.RoutesUser


class CheckableSpinnerAdapter<T> internal constructor(context: Context, headerText: String, all_items: ArrayList<SpinnerItem<T>>, selected_items: MutableSet<T>) : BaseAdapter() {

    internal class SpinnerItem<T>(val item: T, val txt: String)

    private val context: Context = context
    val selected_items: MutableSet<T> = selected_items
    private val all_items: List<SpinnerItem<T>> = all_items
    private val headerText: String = headerText
    private val _checkValue : MutableLiveData<Boolean> = MutableLiveData(false)
    private val listCheckBox: ArrayList<CheckBox> = ArrayList()
    private var selectAllChecked: Boolean = false
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
        var convertView = convertView
        val holder: ViewHolder
        if (convertView == null) {
            val layoutInflator = LayoutInflater.from(context)
            convertView = layoutInflator.inflate(R.layout.row_user, parent, false)
            holder = ViewHolder()
            holder.mTextView = convertView.findViewById(R.id.tvUserAdapter)
            holder.mCheckBox = convertView.findViewById(R.id.cbUserAdapter)
            holder.mLayout = convertView.findViewById(R.id.viewUserAdapter)
            listCheckBox.add(holder.mCheckBox!!)
            convertView.tag = holder
        } else {
            holder = convertView.tag as CheckableSpinnerAdapter<T>.ViewHolder
        }

        val listPos = position - 1
        if (position < 1) {
            holder.mCheckBox?.visibility = View.GONE
            holder.mTextView?.text = headerText
        }else {
            holder.mCheckBox?.visibility = View.VISIBLE
            holder.mTextView?.text = all_items[listPos].txt
            val item = all_items[listPos].item
            Log.d("debug",holder.mTextView?.text.toString())
            if(holder.mTextView?.text != "Selecionar todos"){
                Log.d("debug", "entre")
                val params = holder.mLayout?.layoutParams as ConstraintLayout.LayoutParams
                params.setMargins(60,0,0,0)
                holder.mLayout?.layoutParams = params
            }
            if(!selectAllChecked){
                holder.mCheckBox?.setOnCheckedChangeListener(null)
                val isSel = selected_items.contains(item)
                holder.mCheckBox?.isChecked = isSel
            }
            //Log.d("selected_items ---->>",selected_items.toString())
            holder.mCheckBox?.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
                Log.d("id",buttonView.id.toString())
                if(holder.mTextView?.text != "Selecionar todos"){
                    Log.d("debug", "entre")
                    val params = holder.mLayout?.layoutParams as ConstraintLayout.LayoutParams
                    params.setMargins(60,0,0,0)
                    holder.mLayout?.layoutParams = params
                }
                if(all_items[listPos].txt == "Selecionar todos"){
                    //Log.d("asd", "entre")
                    if(isChecked){
                        selected_items.clear()
                        selectAllChecked = true
                        Log.d("selected_items", "checked")
                        for(i in listCheckBox){
                            i.isChecked = true
                        }
                    }else{
                        for(i in listCheckBox){
                            i.isChecked = false
                        }
                        selectAllChecked = false
                    }
                }else{
                    if (isChecked) {
                        selected_items.add(item)
                        Log.d("selected list", selected_items.toString())
                    } else {
                        selected_items.remove(item)
                    }
                }
            })
        }
        return convertView
    }

    private inner class ViewHolder {
        var mTextView: TextView? = null
        var mCheckBox: CheckBox? = null
        var mLayout: View? = null
    }

}