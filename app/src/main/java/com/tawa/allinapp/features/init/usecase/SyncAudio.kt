package com.tawa.allinapp.features.init.usecase

import com.tawa.allinapp.core.interactor.UseCase
import com.tawa.allinapp.data.repository.ReportsRepository
import javax.inject.Inject

class SyncAudio
@Inject constructor(private val reportsRepository: ReportsRepository) : UseCase<Boolean, UseCase.None>() {

    override suspend fun run(params: None) = reportsRepository.syncReportAudio()

}