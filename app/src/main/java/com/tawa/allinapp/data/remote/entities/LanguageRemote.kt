package com.tawa.allinapp.data.remote.entities

import com.tawa.allinapp.data.local.models.LanguageModel
import com.tawa.allinapp.models.Language

data class LanguageRemote(
    val View: String,
    val Type: String,
    val Id: String,
    val TextoEspanol: String,
    val TextoIngles: String,
    val NombreXML: String
) {
    fun toModel() = LanguageModel(Id, View, Type, TextoEspanol, TextoIngles, NombreXML)
    fun toView() = Language(View,Type,Id,TextoEspanol,TextoIngles,NombreXML)
}










