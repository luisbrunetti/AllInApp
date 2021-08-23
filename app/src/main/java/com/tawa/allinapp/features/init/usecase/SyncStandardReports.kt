package com.tawa.allinapp.features.init.usecase

import com.tawa.allinapp.core.interactor.UseCase
import com.tawa.allinapp.data.repository.ReportsRepository
import javax.inject.Inject

class SyncStandardReports
@Inject constructor(private val reportsRepository: ReportsRepository) : UseCase<Boolean, SyncStandardReports.Params>() {

    override suspend fun run(params: Params) = reportsRepository.syncReportStandard(params.idReport,params.latitude,params.longitude)
    data class Params(val idReport:String,val latitude:String,val longitude:String)
}