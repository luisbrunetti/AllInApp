package com.tawa.allinapp.data.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.tawa.allinapp.models.Question

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
    val required : Boolean

){
    fun toView() = Question(id,questionName,order,idReport,objectType,idUserCreator,idUserModifier,dateModify,dateCreation,required)
}