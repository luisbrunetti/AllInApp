package com.tawa.allinapp.features.reports.sku

import com.tawa.allinapp.core.interactor.UseCase
import com.tawa.allinapp.data.repository.ReportsRepository
import com.tawa.allinapp.models.SkuDetail
import javax.inject.Inject


class UpdateStateSku
@Inject constructor(private val reportsRepository: ReportsRepository) : UseCase<Boolean, UpdateStateSku.Params>() {

    override suspend fun run(params: Params) = reportsRepository.updateStateSku(params._id,params.idSku,params.state,params.type,params.latitude,params.longitude)
    data class Params(val _id: Int, val idSku: String,val state:String,val type:String,val latitude:String,val longitude:String)
}