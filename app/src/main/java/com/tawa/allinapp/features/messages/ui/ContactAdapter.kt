package com.tawa.allinapp.features.messages.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tawa.allinapp.databinding.RowUserNewgroupBinding
import com.tawa.allinapp.models.QuickBloxUser
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class ContactAdapter
@Inject constructor(val context: Context) : RecyclerView.Adapter<ContactAdapter.ContactAdapterHolder>() {

    var listUsers: ArrayList<QuickBloxUser> = ArrayList()

    var clickListener : (QuickBloxUser) -> Unit = { _ -> }

    fun setData(users: List<QuickBloxUser>){
        listUsers.clear()
        listUsers = ArrayList(users)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactAdapterHolder {
        return ContactAdapterHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ContactAdapterHolder, position: Int) {
        holder.bind(context,listUsers[position],clickListener)
    }

    override fun getItemCount(): Int {
        return listUsers.size
    }
    class ContactAdapterHolder(val binding: RowUserNewgroupBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(context:Context, quickBloxUser: QuickBloxUser, clickListener : (QuickBloxUser) -> Unit){
            binding.rvNameNewGroup.text = quickBloxUser.name
            binding.rvFirstTwoLetters.text = getTwoLettersInName(quickBloxUser.name)
            binding.vContactNewGroup.setOnClickListener {
                clickListener(quickBloxUser)
            }
        }

        companion object{
            fun from(parent: ViewGroup): ContactAdapterHolder{
                val inflater = LayoutInflater.from(parent.context)
                val binding = RowUserNewgroupBinding.inflate(inflater,parent,false)
                return ContactAdapterHolder(binding)
            }
        }
        private fun getTwoLettersInName(name:String): String{
            val firstLetter = name.substring(0,1).uppercase(Locale.ROOT)
            val indexSpace = name.indexOf(" ",0,true)
            val secondLetter = name.substring(indexSpace+1,indexSpace+2).uppercase(Locale.ROOT)
            return "$firstLetter$secondLetter"
        }
    }
}