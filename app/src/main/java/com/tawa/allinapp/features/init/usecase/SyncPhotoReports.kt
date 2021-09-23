package com.tawa.allinapp.features.init.usecase

import com.tawa.allinapp.core.interactor.UseCase
import com.tawa.allinapp.data.repository.ReportsRepository
import javax.inject.Inject

class SyncPhotoReports
@Inject constructor(private val reportsRepository: ReportsRepository) : UseCase<Boolean, SyncPhotoReports.Params>() {

    override suspend fun run(params: Params) = reportsRepository.syncPhotoReports(params.latitude,params.longitude)
    data class Params(val latitude:String,val longitude:String)

}