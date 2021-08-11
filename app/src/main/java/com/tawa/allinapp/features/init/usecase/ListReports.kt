package com.tawa.allinapp.features.init.usecase

import com.tawa.allinapp.core.interactor.UseCase
import com.tawa.allinapp.data.repository.CheckRepository
import com.tawa.allinapp.data.repository.ReportsRepository
import com.tawa.allinapp.models.Report
import javax.inject.Inject

class ListReports
@Inject constructor(private val reportsRepository: ReportsRepository) : UseCase<List<Report>, ListReports.Params>() {

    override suspend fun run(params: Params) = reportsRepository.listReports(params.idCompany)
    data class Params(val idCompany: String)
}