package com.tawa.allinapp.features.pdv

import com.tawa.allinapp.core.interactor.UseCase
import com.tawa.allinapp.data.repository.PdvRepository
import javax.inject.Inject

class UpdatePdvRemote
@Inject constructor(private val pdvRepository: PdvRepository) : UseCase<Boolean, UpdatePdvRemote.Params>() {

    override suspend fun run(params: Params) = pdvRepository.updatePdvRemote(params.idPdv,params.nameUser,params.phoneUser,params.ruc,params.latitude,params.longitude,params.image)
    data class Params(val idPdv: String,val nameUser:String,val phoneUser:String,val ruc:String,val latitude:String,val longitude:String,val image:String)
}