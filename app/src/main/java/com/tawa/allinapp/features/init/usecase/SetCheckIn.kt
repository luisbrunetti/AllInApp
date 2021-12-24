package com.tawa.allinapp.features.init.usecase

import com.tawa.allinapp.core.interactor.UseCase
import com.tawa.allinapp.data.repository.CheckRepository
import com.tawa.allinapp.models.Check
import javax.inject.Inject

class SetCheckIn
@Inject constructor(private val checkRepository: CheckRepository) : UseCase<Boolean, SetCheckIn.Params>() {

    override suspend fun run(params: Params) = checkRepository.setCheck(params.id,params.pv,params.idUser,params.pvName,params.registerDate,params.latitude,params.longitude,params.comment,params.state)
    data class Params(
        val id: Int,
        val pv: String,
        val pvName:String,
        val idUser: String,
        val registerDate: String,
        val latitude: String,
        val longitude: String,
        val comment: String,
        val state:String
    )

}