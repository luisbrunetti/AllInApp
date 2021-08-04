package com.tawa.allinapp.features.reports.picture

import com.tawa.allinapp.core.interactor.UseCase
import com.tawa.allinapp.data.repository.ReportsRepository
import com.tawa.allinapp.models.PhotoReport
import javax.inject.Inject

class GetLocalPhotoReport
@Inject constructor(private val reportsRepository: ReportsRepository) : UseCase<PhotoReport, UseCase.None>() {

    override suspend fun run(params: None) = reportsRepository.getLocalPhotoReport()

}