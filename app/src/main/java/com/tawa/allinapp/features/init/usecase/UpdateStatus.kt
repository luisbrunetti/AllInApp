package com.tawa.allinapp.features.init.usecase

import com.tawa.allinapp.core.interactor.UseCase
import com.tawa.allinapp.data.repository.ParametersRepository
import com.tawa.allinapp.data.repository.ReportsRepository
import com.tawa.allinapp.models.Schedule
import javax.inject.Inject

class UpdateStatus
@Inject constructor(private val reportsRepository: ReportsRepository) : UseCase<Boolean, UpdateStatus.Params>() {

    override suspend fun run(params: Params) = reportsRepository.updateStatus(params.latitude,params.longitude,params.battery)

    data class Params(val latitude: String,val longitude:String,val battery:String)

}