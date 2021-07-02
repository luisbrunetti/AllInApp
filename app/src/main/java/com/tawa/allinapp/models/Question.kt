package com.tawa.allinapp.models

import com.tawa.allinapp.data.remote.entities.QuestionsRemote


data class Question(
    val id: String,
    val status: String,
    val questionName: String,
    val order : Int,
    val idReport: String,
    val objectType: String,
    val idUserCreator: String,
    val idUserModifier: String?,
    val dateModify: String?,
    val dateCreation: String,
)