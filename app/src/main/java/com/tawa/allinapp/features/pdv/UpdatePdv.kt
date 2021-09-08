package com.tawa.allinapp.features.pdv

import com.tawa.allinapp.core.interactor.UseCase
import com.tawa.allinapp.data.repository.PdvRepository
import javax.inject.Inject

class UpdatePdv
@Inject constructor(private val pdvRepository: PdvRepository) : UseCase<Boolean, UpdatePdv.Params>() {

    override suspend fun run(params: Params) = pdvRepository.updatePdv(params.idPdv,params.nameUser,params.phoneUser,params.ruc,params.latitude,params.longitude,params.image,params.state)
    data class Params(val idPdv: String,val nameUser:String,val phoneUser:String,val ruc:String,val latitude:String,val longitude:String,val image:String,val state:String)
}