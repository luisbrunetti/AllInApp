package com.tawa.allinapp.features.init.usecase

import com.tawa.allinapp.core.interactor.UseCase
import com.tawa.allinapp.data.repository.CheckRepository
import java.lang.reflect.Parameter
import javax.inject.Inject

class SyncCheck
@Inject constructor(private val checkRepository: CheckRepository) : UseCase<Boolean, SyncCheck.Params>() {

    override suspend fun run(params: Params) = checkRepository.syncChecks(params.latitude,params.longitude)
    data class Params(val latitude:String,val longitude:String)
}