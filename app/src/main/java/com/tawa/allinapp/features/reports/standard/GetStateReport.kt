package com.tawa.allinapp.features.reports.standard

import com.tawa.allinapp.core.interactor.UseCase
import com.tawa.allinapp.data.repository.QuestionsRepository
import com.tawa.allinapp.data.repository.ReportsRepository
import javax.inject.Inject

class GetStateReport
@Inject constructor(private val reportsRepository: ReportsRepository) : UseCase<String, GetStateReport.Params>() {

    override suspend fun run(params: Params) = reportsRepository.getStateReport(params.idReport)
    data class  Params(val idReport:String)
}