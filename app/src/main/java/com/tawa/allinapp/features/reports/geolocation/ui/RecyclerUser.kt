package com.tawa.allinapp.features.reports.geolocation.ui

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tawa.allinapp.R
import com.tawa.allinapp.models.RoutesUser


open class RecyclerUser (data: ArrayList<RoutesUser>, var context: Context?):
    RecyclerView.Adapter<RecyclerUser.ViewHolder>() {
    var mData: ArrayList<RoutesUser>? = data
    private var mAllData: ArrayList<RoutesUser> = data
    private var mInflater: LayoutInflater? = LayoutInflater.from(this.context)
    var listChecked: ArrayList<RoutesUser> = ArrayList()
    val listChexBoxes: HashSet<CheckBox> = hashSetOf()
    private var allSelected : Boolean = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = mInflater!!.inflate(R.layout.row_user, parent, false)
        val viewHolder = ViewHolder(view)
        listChexBoxes.add(viewHolder.mCheckBox!!)
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(mData!![position])
    }

    override fun getItemCount(): Int {
        return mData!!.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun filteredList(newList : ArrayList<RoutesUser>){
        mData?.clear()
        mData?.addAll(newList)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mName: TextView? = itemView.findViewById(R.id.tvUserAdapter)
        var mCheckBox: CheckBox? = itemView.findViewById(R.id.cbUserAdapter)
        fun bindData(item: RoutesUser){
            if(!allSelected){
                mCheckBox?.setOnCheckedChangeListener(null)
                mCheckBox?.isChecked = listChecked.contains(item)
            }else{
                mCheckBox?.isChecked = true
            }
            mName?.text = item.name
            mCheckBox?.setOnCheckedChangeListener { buttonView, isChecked ->
                Log.d("id",isChecked.toString())
                /*if(mName.text != "Selecionar todos"){
                    Log.d("debug", "entre")
                    val params = holder.mLayout?.layoutParams as ConstraintLayout.LayoutParams
                    params.setMargins(60,0,0,0)
                    holder.mLayout?.layoutParams = params
                }*/
                if(item.name == "Selecionar todos"){
                    if(isChecked){
                        listChecked.clear()
                        for(i in listChexBoxes){ i.isChecked = true }
                        for(j in mAllData){ listChecked.add(j)}
                        Log.d(" -- >",listChecked.toString())
                        allSelected = true
                    }else{
                        for(i in listChexBoxes){ i.isChecked = false }
                        listChecked.clear()
                        allSelected = false
                    }
                }
                else{
                    if(isChecked){ listChecked.add(item) }
                    else{ listChecked.remove(item) }
                }
            }
        }
    }
}