package com.tawa.allinapp.features.reports.audiodinamico.usecase

import com.tawa.allinapp.core.interactor.UseCase
import com.tawa.allinapp.data.repository.QuestionsRepository
import com.tawa.allinapp.models.Answer
import javax.inject.Inject

class GetAnswerAudio
@Inject
constructor(private val questionsRepository: QuestionsRepository) : UseCase<List<Answer>, GetAnswerAudio.Params>() {

    override suspend fun run(params: Params) = questionsRepository.getAnswers(params.idQuestions)
    data class Params(val idQuestions: String)
}