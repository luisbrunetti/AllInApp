package com.tawa.allinapp.data.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.tawa.allinapp.data.remote.entities.QuestionsRemote
import com.tawa.allinapp.features.init.usecase.SetIdCompany
import com.tawa.allinapp.models.Answer
import com.tawa.allinapp.models.Company
import com.tawa.allinapp.models.Question
import com.tawa.allinapp.models.Report

@Entity(tableName = "questions")
data class QuestionModel(
    @PrimaryKey val id: String,
    val questionName: String,
    val order : Int,
    val idReport: String,
    val objectType: String,
    val idUserCreator: String,
    val idUserModifier: String?,
    val dateModify: String?,
    val dateCreation: String,

){
    fun toView() = Question(id,questionName,order,idReport,objectType,idUserCreator,idUserModifier,dateModify,dateCreation)
}