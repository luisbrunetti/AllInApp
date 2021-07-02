package com.tawa.allinapp.features.init.usecase

import com.tawa.allinapp.core.interactor.UseCase
import com.tawa.allinapp.data.repository.ReportsRepository
import javax.inject.Inject


class GetReportsRemote
@Inject constructor(private val reportsRepository: ReportsRepository) : UseCase<Boolean, GetReportsRemote.Params>() {

    override suspend fun run(params: Params) = reportsRepository.setReports(params.company)

    class Params(val company: String)

}