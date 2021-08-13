package com.tawa.allinapp.features.reports.standard

import com.tawa.allinapp.core.interactor.UseCase
import com.tawa.allinapp.data.repository.QuestionsRepository
import com.tawa.allinapp.data.repository.ReportsRepository
import javax.inject.Inject

class SetAnswerPv
@Inject constructor(private val questionsRepository: QuestionsRepository) : UseCase<Boolean, SetAnswerPv.Params>() {

    override suspend fun run(params: Params) = questionsRepository.setAnswersPv(params.idAnswer,params.idQuestion,params.nameAnswer,params.img)
    data class Params(val idAnswer:String,val idQuestion:String,val nameAnswer:String,val img:String)
}