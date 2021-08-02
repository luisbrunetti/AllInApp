package com.tawa.allinapp.features.reports.standard

import com.tawa.allinapp.core.interactor.UseCase
import com.tawa.allinapp.data.repository.CheckRepository
import com.tawa.allinapp.data.repository.QuestionsRepository
import com.tawa.allinapp.models.Check
import com.tawa.allinapp.models.ReadyAnswer
import javax.inject.Inject

class SetReadyAnswers
@Inject constructor(private val questionsRepository: QuestionsRepository) : UseCase<Boolean, SetReadyAnswers.Params>() {

    override suspend fun run(params: Params) = questionsRepository.setReadyAnswers(ReadyAnswer(params.id,params.idQuestion,params.nameQuestion,params.idAnswer,params.nameAnswer,params.img))
    data class Params(
        val id: Int,
        val idQuestion: String,
        val nameQuestion: String,
        val idAnswer : String,
        val nameAnswer: String,
        val img : String
    )

}