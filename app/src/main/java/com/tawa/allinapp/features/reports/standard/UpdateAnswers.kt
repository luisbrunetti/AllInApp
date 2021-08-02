package com.tawa.allinapp.features.reports.standard

import com.tawa.allinapp.core.interactor.UseCase
import com.tawa.allinapp.data.repository.QuestionsRepository
import com.tawa.allinapp.models.Answer
import com.tawa.allinapp.models.Question
import javax.inject.Inject

class UpdateAnswers
@Inject constructor(private val questionsRepository: QuestionsRepository) : UseCase<Boolean, UpdateAnswers.Params>() {

    override suspend fun run(params: Params) = questionsRepository.updateAnswers(params.idAnswer,params.data)
    data class Params(val idAnswer: String,val data:String)
}