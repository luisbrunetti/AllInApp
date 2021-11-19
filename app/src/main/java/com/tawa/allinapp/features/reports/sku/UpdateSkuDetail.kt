package com.tawa.allinapp.features.reports.sku

import com.tawa.allinapp.core.interactor.UseCase
import com.tawa.allinapp.data.repository.ReportsRepository
import com.tawa.allinapp.models.Sku
import com.tawa.allinapp.models.SkuDetail
import javax.inject.Inject


class UpdateSkuDetail
@Inject constructor(private val reportsRepository: ReportsRepository) : UseCase<Boolean, UpdateSkuDetail.Params>() {

    override suspend fun run(params: Params) = reportsRepository.updateSkuDetail(params.idSkuDetail,params.idPv,params.stock,params.exhibition,params.price)
    data class Params(val idSkuDetail: String,val idPv: String, val stock:Boolean,val exhibition:Boolean,val price:Float)
}