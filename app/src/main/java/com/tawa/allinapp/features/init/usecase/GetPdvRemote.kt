package com.tawa.allinapp.features.init.usecase

import com.tawa.allinapp.core.interactor.UseCase
import com.tawa.allinapp.data.repository.CheckRepository
import com.tawa.allinapp.data.repository.PdvRepository
import javax.inject.Inject

class GetPdvRemote
@Inject constructor(private val pdvRepository: PdvRepository) : UseCase<Boolean, GetPdvRemote.Params>() {

    override suspend fun run(params: Params) = pdvRepository.getPdvRemote(params.idCompany)
    data class Params(val idCompany: String)
}