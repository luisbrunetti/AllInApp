package com.tawa.allinapp.data.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.tawa.allinapp.data.remote.entities.QuestionsRemote
import com.tawa.allinapp.features.init.usecase.SetIdCompany
import com.tawa.allinapp.models.Answer
import com.tawa.allinapp.models.Company
import com.tawa.allinapp.models.Question
import com.tawa.allinapp.models.Report

@Entity(tableName = "answers")
data class AnswerModel(
    @PrimaryKey val id: String,
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
    val row:Boolean,
    val column:Boolean,
    val orderQ:String

){
    fun toView() = Answer(id,status,answerName,order,idQuestion,idUserCreator,idUserModifier,dateModify,dateCreation,data,nameQuestion,objectType,row,column,orderQ)
}