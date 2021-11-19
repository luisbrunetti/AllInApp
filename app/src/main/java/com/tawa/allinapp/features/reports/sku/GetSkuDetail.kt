package com.tawa.allinapp.features.reports.sku

import com.tawa.allinapp.core.interactor.UseCase
import com.tawa.allinapp.data.repository.ReportsRepository
import com.tawa.allinapp.models.SkuDetail
import javax.inject.Inject


class GetSkuDetail
@Inject constructor(private val reportsRepository: ReportsRepository) : UseCase<List<SkuDetail>, GetSkuDetail.Params>() {

    override suspend fun run(params: Params) = reportsRepository.getSkuDetail(params.idSku, params.idPv)
    data class Params(val idSku: String, val idPv:String)
}