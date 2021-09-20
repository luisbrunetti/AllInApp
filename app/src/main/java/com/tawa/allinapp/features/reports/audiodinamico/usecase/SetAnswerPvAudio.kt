package com.tawa.allinapp.features.reports.audiodinamico.usecase

import com.tawa.allinapp.core.interactor.UseCase
import com.tawa.allinapp.data.local.datasource.QuestionsDataSource
import com.tawa.allinapp.data.repository.QuestionsRepository
import com.tawa.allinapp.features.reports.standard.SetAnswerPv
import javax.inject.Inject

class SetAnswerPvAudio
@Inject constructor(private val questionsRepository: QuestionsRepository) : UseCase<Boolean, SetAnswerPvAudio.Params>() {

    override suspend fun run(params: Params) = questionsRepository.setAnswersPv(params.idAnswer,params.idQuestion,params.nameAnswer,params.img)
    data class Params(val idAnswer:String,val idQuestion:String,val nameAnswer:String,val img:String)
}