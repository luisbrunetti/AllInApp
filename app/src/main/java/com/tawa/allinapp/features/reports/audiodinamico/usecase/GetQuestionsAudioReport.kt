package com.tawa.allinapp.features.reports.audiodinamico.usecase

import com.tawa.allinapp.core.interactor.UseCase
import com.tawa.allinapp.data.repository.QuestionsRepository
import com.tawa.allinapp.models.Answer
import javax.inject.Inject

class GetQuestionsAudioReport
@Inject
constructor(private val questionsRepository: QuestionsRepository) : UseCase<List<Answer>, GetQuestionsAudioReport.Params>() {

    override suspend fun run(params: Params) = questionsRepository.getQuestions(params.idReport)
    data class Params(val idReport: String)
}