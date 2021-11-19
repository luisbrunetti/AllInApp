package com.tawa.allinapp.features.reports.sku

import com.tawa.allinapp.core.interactor.UseCase
import com.tawa.allinapp.data.repository.ReportsRepository
import com.tawa.allinapp.models.SkuDetail
import com.tawa.allinapp.models.SkuObservation
import javax.inject.Inject


class GetSkuObservation
@Inject constructor(private val reportsRepository: ReportsRepository) : UseCase<List<SkuObservation>, GetSkuObservation.Params>() {

    override suspend fun run(params: Params) = reportsRepository.getSkuObservation(params.idSkuDetail,params.idPv)
     data class Params(val idSkuDetail: String,val idPv: String)
}