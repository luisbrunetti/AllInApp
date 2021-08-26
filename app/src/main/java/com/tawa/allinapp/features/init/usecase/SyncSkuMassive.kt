package com.tawa.allinapp.features.init.usecase

import com.tawa.allinapp.core.interactor.UseCase
import com.tawa.allinapp.data.repository.ReportsRepository
import javax.inject.Inject

class SyncSkuMassive
@Inject constructor(private val reportsRepository: ReportsRepository) : UseCase<Boolean, SyncSkuMassive.Params>() {

    override suspend fun run(params: Params) = reportsRepository.syncSkuMassive(params.latitude,params.longitude)
    data class Params(val latitude:String,val longitude:String)
}