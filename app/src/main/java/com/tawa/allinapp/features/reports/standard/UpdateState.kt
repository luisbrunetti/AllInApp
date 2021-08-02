package com.tawa.allinapp.features.reports.standard

import com.tawa.allinapp.core.interactor.UseCase
import com.tawa.allinapp.data.repository.QuestionsRepository
import javax.inject.Inject

class UpdateState
@Inject constructor(private val questionsRepository: QuestionsRepository) : UseCase<Boolean, UpdateState.Params>() {

    override suspend fun run(params: Params) = questionsRepository.changeState(params.state,params.verify)
    data class Params(val state:Boolean,val verify:Boolean)
}