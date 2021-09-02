package com.tawa.allinapp.models



data class Answer(
    val id: String,
    val status: String,
    val answerName: String,
    val order : Int,
    val idQuestion: String,
    val idUserCreator: String,
    val idUserModifier: String?,
    val dateModify: String?,
    val dateCreation: String,
    val data:String,
    val nameQuestion: String,
    val objectType :String,
)