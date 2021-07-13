package com.tawa.allinapp.features.reports.userstatus

import com.tawa.allinapp.core.interactor.UseCase
import com.tawa.allinapp.data.repository.ReportsRepository
import com.tawa.allinapp.models.ReportStatus
import javax.inject.Inject


class GetReportStatus
@Inject constructor(private val reportsRepository: ReportsRepository) : UseCase<List<ReportStatus>, UseCase.None>() {

    override suspend fun run(params: None) = reportsRepository.getReportStatus()

}