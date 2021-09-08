package com.tawa.allinapp.features.pdv

import com.tawa.allinapp.core.interactor.UseCase
import com.tawa.allinapp.data.repository.CheckRepository
import com.tawa.allinapp.data.repository.PdvRepository
import com.tawa.allinapp.models.Pdv
import javax.inject.Inject

class GetPdv
@Inject constructor(private val pdvRepository: PdvRepository) : UseCase<Pdv, GetPdv.Params>() {

    override suspend fun run(params: Params) = pdvRepository.getPdv(params.idPdv)
    data class Params(val idPdv:String)
}