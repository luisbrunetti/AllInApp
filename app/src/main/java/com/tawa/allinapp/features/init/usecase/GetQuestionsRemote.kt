package com.tawa.allinapp.features.init.usecase

import com.tawa.allinapp.core.interactor.UseCase
import com.tawa.allinapp.data.repository.QuestionsRepository
import javax.inject.Inject


class GetQuestionsRemote
@Inject constructor(private val questionsRepository: QuestionsRepository) : UseCase<Boolean, GetQuestionsRemote.Params>() {

    override suspend fun run(params: Params) = questionsRepository.setQuestions(params.idReport)
    data class Params(val idReport:String)

}