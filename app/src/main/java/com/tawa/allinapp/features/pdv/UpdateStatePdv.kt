package com.tawa.allinapp.features.pdv

import com.tawa.allinapp.core.interactor.UseCase
import com.tawa.allinapp.data.repository.PdvRepository
import javax.inject.Inject

class UpdateStatePdv
@Inject constructor(private val pdvRepository: PdvRepository) : UseCase<Boolean, UpdateStatePdv.Params>() {

    override suspend fun run(params: Params) = pdvRepository.updatePdv(params.idPdv,params.state)
    data class Params(val idPdv: String,val state:String)
}