package com.tawa.allinapp.features.reports.standard

import com.tawa.allinapp.core.interactor.UseCase
import com.tawa.allinapp.data.repository.QuestionsRepository
import com.tawa.allinapp.data.repository.ReportsRepository
import javax.inject.Inject

class UpdateStateReport
@Inject constructor(private val reportsRepository: ReportsRepository) : UseCase<Boolean, UpdateStateReport.Params>() {

    override suspend fun run(params: Params) = reportsRepository.updateStateReport(params.idReport,params.state)
    data class Params(val idReport:String,val state:String)
}