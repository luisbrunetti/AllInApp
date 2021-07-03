package com.tawa.allinapp.features.reports.reports

import com.tawa.allinapp.core.interactor.UseCase
import com.tawa.allinapp.data.repository.ReportsRepository
import com.tawa.allinapp.models.Report
import javax.inject.Inject


class GetReports
@Inject constructor(private val reportsRepository: ReportsRepository) : UseCase<List<Report>, UseCase.None>() {

    override suspend fun run(params: None) = reportsRepository.getReports()

}