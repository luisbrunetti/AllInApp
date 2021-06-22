package com.tawa.allinapp.features.init.usecase

import com.tawa.allinapp.core.interactor.UseCase
import com.tawa.allinapp.data.repository.CheckRepository
import com.tawa.allinapp.models.CheckIn
import javax.inject.Inject

class SetCheckIn
@Inject constructor(private val checkRepository: CheckRepository) : UseCase<Boolean, SetCheckIn.Params>() {

    override suspend fun run(params: Params) = checkRepository.setCheckIn(CheckIn(params.id,params.idUser,params.idPV,params.registerDate,params.latitude,params.longitude,params.comment))
    data class Params(
        val id: Int,
        val idUser: String,
        val idPV: String,
        val registerDate: String,
        val latitude: String,
        val longitude: String,
        val comment: String,
    )

}