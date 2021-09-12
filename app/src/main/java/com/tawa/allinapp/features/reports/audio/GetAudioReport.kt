package com.tawa.allinapp.features.reports.audio

import com.tawa.allinapp.core.interactor.UseCase
import com.tawa.allinapp.data.local.models.AudioReportModel
import com.tawa.allinapp.data.repository.QuestionsRepository
import com.tawa.allinapp.data.repository.ReportsRepository
import com.tawa.allinapp.models.AudioReport
import com.tawa.allinapp.models.Question
import javax.inject.Inject

class GetAudioReport
@Inject constructor(private val  reportsRepository: ReportsRepository) : UseCase<AudioReport, GetAudioReport.Params>() {
    override suspend fun run(params: Params) = reportsRepository.getAudioReport(params.idPv, params.idUser)

    data class Params(val idPv:String, val idUser: String)

}