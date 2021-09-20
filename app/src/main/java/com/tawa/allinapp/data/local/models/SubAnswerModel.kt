package com.tawa.allinapp.data.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.tawa.allinapp.data.remote.entities.QuestionsRemote
import com.tawa.allinapp.features.init.usecase.SetIdCompany
import com.tawa.allinapp.models.*

@Entity(tableName = "sub_answers")
data class SubAnswerModel(
    @PrimaryKey val id: String,
    val subAnswerName: String,
    val order : String,
    val idQuestion: String,
){
    fun toView() =SubAnswer(id,subAnswerName,order,idQuestion)
}