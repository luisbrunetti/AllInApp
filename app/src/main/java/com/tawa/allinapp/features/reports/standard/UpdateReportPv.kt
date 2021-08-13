package com.tawa.allinapp.features.reports.standard

import com.tawa.allinapp.core.interactor.UseCase
import com.tawa.allinapp.data.repository.ReportsRepository
import javax.inject.Inject

class UpdateReportPv
@Inject constructor(private val reportsRepository: ReportsRepository) : UseCase<Boolean, UpdateReportPv.Params>() {

    override suspend fun run(params: Params) = reportsRepository.updateReportPv(params.idReport,params.state,params.type)
    data class Params(val idReport:String,val state:String,val type:String)
}