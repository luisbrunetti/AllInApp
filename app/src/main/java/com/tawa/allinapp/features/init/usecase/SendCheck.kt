package com.tawa.allinapp.features.init.usecase

import com.tawa.allinapp.core.interactor.UseCase
import com.tawa.allinapp.data.repository.CheckRepository
import com.tawa.allinapp.models.Check
import javax.inject.Inject

class SendCheck
@Inject constructor(private val checkRepository: CheckRepository) : UseCase<String, SendCheck.Params>() {

    override suspend fun run(params: Params) = checkRepository.sendCheck(params.latitude,params.longitude,params.type)
    data class Params(
        val latitude: String,
        val longitude: String,
        val type: Int,
    )

}