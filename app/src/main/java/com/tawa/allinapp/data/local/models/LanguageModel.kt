package com.tawa.allinapp.data.local.models

import androidx.room.Entity
import com.tawa.allinapp.models.Language

@Entity(tableName = "Language",primaryKeys = ["id"])
data class LanguageModel(
    val id: String,
    val view : String,
    val type : String,
    val spanishText : String,
    val englishText : String,
    val xmlName : String
){
    fun toView() = Language(view,type,id,spanishText,englishText,xmlName)
}