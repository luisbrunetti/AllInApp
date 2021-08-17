package com.tawa.allinapp.features.init.usecase

import com.tawa.allinapp.core.interactor.UseCase
import com.tawa.allinapp.data.repository.CheckRepository
import com.tawa.allinapp.data.repository.ReportsRepository
import javax.inject.Inject

class GetReportsSku
@Inject constructor(private val reportsRepository: ReportsRepository) : UseCase<Boolean, GetReportsSku.Params>() {

    override suspend fun run(params: Params) = reportsRepository.getReportsSku(params.company)
    data class  Params(val company: String)
}