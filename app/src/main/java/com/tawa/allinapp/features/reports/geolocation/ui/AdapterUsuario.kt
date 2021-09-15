package com.tawa.allinapp.features.reports.geolocation.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.TextView
import com.tawa.allinapp.R
import com.tawa.allinapp.models.RoutesUser
import kotlin.properties.Delegates


class AdapterUsuario(
    private var conx: Context,
    private val resource : Int,
    private val list: List<RoutesUser>
) : ArrayAdapter<RoutesUser>(conx,resource,list) {
    private var checked: Boolean = false
    var collection: List<RoutesUser> by Delegates.observable(emptyList()) {
            _, _, _ -> notifyDataSetChanged()
    }

    var clickListener: (RoutesUser) -> Unit = { _ -> }

    override fun getDropDownView(position: Int, convertView: View, parent: ViewGroup): View {
        return getCustomView(position, convertView, parent)
    }


    private fun getCustomView(
        position: Int, convertView: View?,
        parent: ViewGroup?
    ): View {
        var convertView = convertView
        val holder: ViewHolder
        if (convertView == null) {
            val layoutInflater = LayoutInflater.from(conx)
            convertView = layoutInflater.inflate(R.layout.row_user, null)
            holder = ViewHolder()
            holder.mTextView = convertView?.findViewById<View>(R.id.tvUserAdapter) as TextView
            holder.mCheckBox = convertView.findViewById<View>(R.id.cbUserAdapter) as CheckBox
            convertView.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
        }
        holder.mTextView?.text = (list[position].name)

        // To check weather checked event fire from getview() or user input
        checked = true
        holder.mCheckBox?.isChecked = false
        checked = false
        if (position == 0) {
            holder.mCheckBox?.setVisibility(View.INVISIBLE)
        } else {
            holder.mCheckBox?.setVisibility(View.VISIBLE)
        }
        holder.mCheckBox?.setTag(position)
        holder.mCheckBox?.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            val getPosition = buttonView.tag as Int
            if (!checked) {
                //list.get(position).setSelected(isChecked)
            }
        })
        return convertView
    }

    private class ViewHolder {
        var mTextView: TextView? = null
        var mCheckBox: CheckBox? = null
    }
/*
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvName = itemView.findViewById<TextView>(R.id.tvUserAdapter)
        private val cbName = itemView.findViewById<CheckBox>(R.id.cbUserAdapter)

        fun bind(routesUser: RoutesUser, clickListener: (RoutesUser) -> Unit) {
            tvName.text = routesUser.name
            itemView.setOnClickListener {
                clickListener(routesUser)
            }
            //itemView.setOnClickListener { clickListener(RoutesUser) }
        }
    }*/
}