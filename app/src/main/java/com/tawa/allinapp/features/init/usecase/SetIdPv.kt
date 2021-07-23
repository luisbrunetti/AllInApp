package com.tawa.allinapp.features.init.usecase

import com.tawa.allinapp.core.interactor.UseCase
import com.tawa.allinapp.data.repository.CheckRepository
import javax.inject.Inject

class SetIdPv
@Inject constructor(private val checkRepository: CheckRepository) : UseCase<Boolean, SetIdPv.Params>() {

    override suspend fun run(params: Params) = checkRepository.setIdPv(params.idPv, params.namePv)

    data class Params(val idPv: String, val namePv: String)

}