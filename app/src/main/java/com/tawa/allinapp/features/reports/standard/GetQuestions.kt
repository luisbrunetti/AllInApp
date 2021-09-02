package com.tawa.allinapp.features.reports.standard

import com.tawa.allinapp.core.interactor.UseCase
import com.tawa.allinapp.data.repository.QuestionsRepository
import com.tawa.allinapp.models.Answer
import com.tawa.allinapp.models.Question
import javax.inject.Inject

class GetQuestions
@Inject constructor(private val questionsRepository: QuestionsRepository) : UseCase<List<Answer>, GetQuestions.Params>() {

    override suspend fun run(params: Params) = questionsRepository.getQuestions(params.idReport)
    data class Params(val idReport: String)
}