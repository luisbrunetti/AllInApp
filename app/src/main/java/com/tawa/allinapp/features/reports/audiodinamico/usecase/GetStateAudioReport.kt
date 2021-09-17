package com.tawa.allinapp.features.reports.audiodinamico.usecase

import com.tawa.allinapp.core.interactor.UseCase
import com.tawa.allinapp.data.repository.ReportsRepository
import com.tawa.allinapp.features.reports.standard.GetStateReport
import javax.inject.Inject

class GetStateAudioReport
@Inject constructor(private val reportsRepository: ReportsRepository) : UseCase<String, GetStateAudioReport.Params>() {

    override suspend fun run(params: Params) = reportsRepository.getStateReport(params.idReport)
    data class  Params(val idReport:String)
}