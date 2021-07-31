package com.tawa.allinapp.features.reports.sku

import com.tawa.allinapp.core.interactor.UseCase
import com.tawa.allinapp.data.repository.ReportsRepository
import com.tawa.allinapp.models.SkuDetail
import com.tawa.allinapp.models.SkuObservation
import javax.inject.Inject


class SetSkuObservation
@Inject constructor(private val reportsRepository: ReportsRepository) : UseCase<Boolean, SetSkuObservation.Params>() {

    override suspend fun run(params: Params) = reportsRepository.insertSkuObservation(params.skuObservation)
    data class Params(val skuObservation: SkuObservation)
}