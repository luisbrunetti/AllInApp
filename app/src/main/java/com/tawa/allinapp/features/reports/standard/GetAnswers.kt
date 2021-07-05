package com.tawa.allinapp.features.reports.standard

import com.tawa.allinapp.core.interactor.UseCase
import com.tawa.allinapp.data.repository.QuestionsRepository
import com.tawa.allinapp.models.Answer
import com.tawa.allinapp.models.Question
import javax.inject.Inject

class GetAnswers
@Inject constructor(private val questionsRepository: QuestionsRepository) : UseCase<List<Answer>, GetAnswers.Params>() {

    override suspend fun run(params: Params) = questionsRepository.getAnswers(params.idQuestion)
    data class Params(val idQuestion: String)
}