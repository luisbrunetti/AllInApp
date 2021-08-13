package com.tawa.allinapp.features.reports

import com.tawa.allinapp.core.interactor.UseCase
import com.tawa.allinapp.data.repository.ReportsRepository
import javax.inject.Inject


class GetStateSku
@Inject constructor(private val reportsRepository: ReportsRepository) : UseCase<String, GetStateSku.Params>() {

    override suspend fun run(params: Params) = reportsRepository.getStateSku(params.idPv)

    class Params(val idPv:String)

}