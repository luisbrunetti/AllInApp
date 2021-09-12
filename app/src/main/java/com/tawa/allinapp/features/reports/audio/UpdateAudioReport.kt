package com.tawa.allinapp.features.reports.audio

import com.tawa.allinapp.core.interactor.UseCase
import com.tawa.allinapp.data.local.models.AudioReportModel
import com.tawa.allinapp.data.repository.ReportsRepository
import com.tawa.allinapp.models.AudioReport
import javax.inject.Inject

class UpdateAudioReport
@Inject constructor(private val reportsRepository: ReportsRepository) :
    UseCase<Boolean, AudioReport>() {

    override suspend fun run(params: AudioReport) = reportsRepository.updateAudioReport(
        params.idPv,
        params.idUser,
        params.selected,
        params.selectedName,
        params.record,
        params.recordPath,
        params.recordSent,
        params.comments!!
    )


}