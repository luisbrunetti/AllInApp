package com.tawa.allinapp.features.messages.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tawa.allinapp.databinding.ItemUserSelectedBinding
import com.tawa.allinapp.models.QuickBloxUser
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class SelectedUserAdapter
@Inject constructor(val context: Context) : RecyclerView.Adapter<SelectedUserAdapter.SelectedUserViewHolder>() {

    var selectedUsers : ArrayList<QuickBloxUser> = ArrayList()
    var listenerDeleteUser : (QuickBloxUser, Int) -> Unit = {_,_ -> }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectedUserViewHolder {
        return SelectedUserViewHolder.from(parent,listenerDeleteUser)
    }

    override fun onBindViewHolder(holder: SelectedUserViewHolder, position: Int) {
        holder.bind(selectedUsers[position],position)
    }

    override fun getItemCount(): Int {
        return selectedUsers.size
    }

    fun setData(user: List<QuickBloxUser>){
        selectedUsers.clear()
        selectedUsers = ArrayList(user)
        notifyDataSetChanged()
    }
    class SelectedUserViewHolder(val binding : ItemUserSelectedBinding, val listener : (QuickBloxUser, Int) -> Unit): RecyclerView.ViewHolder(binding.root) {
        fun bind(quickBloxUser: QuickBloxUser, position: Int){
            binding.tvFullName.text = formatName(quickBloxUser.name)
            binding.tvFirstTwoLetters.text = getTwoLettersInName(quickBloxUser.name)
            binding.ibDeleteUserSelected.setOnClickListener {
                listener(quickBloxUser,position)
            }
        }
        companion object{
            fun from(parent:ViewGroup,listener : (QuickBloxUser, Int) -> Unit): SelectedUserViewHolder{
                val inflater = LayoutInflater.from(parent.context)
                val binding = ItemUserSelectedBinding.inflate(inflater,parent,false)
                return SelectedUserViewHolder(binding,listener)
            }
        }
        private fun getTwoLettersInName(name:String): String{
            val firstLetter = name.substring(0,1).uppercase(Locale.ROOT)
            val indexSpace = name.indexOf(" ",0,true)
            val secondLetter = name.substring(indexSpace+1,indexSpace+2).uppercase(Locale.ROOT)
            return "$firstLetter$secondLetter"
        }
        private fun formatName(name: String): String{
            return if(name.length <= 9){ name }
            else name.substring(0,10).plus("...")

        }
    }
}