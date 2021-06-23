package com.tawa.allinapp.features.init.usecase

import com.tawa.allinapp.core.interactor.UseCase
import com.tawa.allinapp.data.repository.CheckRepository
import javax.inject.Inject

class GetStateCheck
@Inject constructor(private val checkRepository: CheckRepository) : UseCase<Boolean, GetStateCheck.Params>() {

    override suspend fun run(params: Params) = checkRepository.getStateCheck(params.idPv)
    data class Params(val idPv:String)


}