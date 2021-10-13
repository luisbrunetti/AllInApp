package com.tawa.allinapp.data.remote.entities

import com.tawa.allinapp.data.local.models.LanguageModel
import com.tawa.allinapp.models.Language
import com.google.gson.annotations.SerializedName


data class LanguageRemote(
    @SerializedName("englishText")
    val englishText: String, // Welcome
    @SerializedName("Id")
    val id: String, // tvHellowLoginFragment
    @SerializedName("spanishText")
    val spanishText: String, // ¡Hola!
    @SerializedName("Type")
    val type: String, // Text
    @SerializedName("View")
    val view: String, // TextView
    @SerializedName("XMLname")
    val xmlName: String // fragment_login.xml
){
    fun toModel() = LanguageModel(id, view, type, spanishText, englishText, xmlName)
    fun toView() = Language(view,type,id,spanishText,englishText,xmlName)
}









