package com.tawa.allinapp.features.reports.audio

import com.tawa.allinapp.core.interactor.UseCase
import com.tawa.allinapp.data.repository.ReportsRepository
import com.tawa.allinapp.models.AudioReport
import com.tawa.allinapp.models.PhotoReport
import javax.inject.Inject


class SetAudioReport
@Inject constructor(private val reportsRepository: ReportsRepository) : UseCase<Boolean, SetAudioReport.Params>() {

    override suspend fun run(params: Params) = reportsRepository.saveLocalAudioReport(params.report)

    class Params(val report: AudioReport)

}