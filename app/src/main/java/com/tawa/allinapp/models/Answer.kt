package com.tawa.allinapp.models



data class Answer(
    val id: String,
    val status: String,
    val answerName: String,
    val order : String,
    val idQuestion: String,
    val idUserCreator: String,
    val idUserModifier: String?,
    val dateModify: String?,
    val dateCreation: String,
    val data:String,
    val nameQuestion: String,
    val objectType :String,
    val row : Boolean,
    val column : Boolean,
    val orderQ:String,
    val required:Boolean,
    val path : String,
)