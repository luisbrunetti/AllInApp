package com.tawa.allinapp.features.init.usecase

import com.tawa.allinapp.core.interactor.UseCase
import com.tawa.allinapp.data.local.models.CheckinModel
import com.tawa.allinapp.data.repository.ParametersRepository
import com.tawa.allinapp.models.Checkin
import javax.inject.Inject

class SetCheckin
@Inject constructor(private val parametersRepository: ParametersRepository) : UseCase<Boolean, SetCheckin.Params>() {

    override suspend fun run(params: Params) = parametersRepository.setCheckin(CheckinModel(params.id,params.idUser,params.idPuntoventa,params.fechaRegistro,params.latitud,params.longitud,params.comment))
    data class Params(
        val id: Int,
        val idUser: String,
        val idPuntoventa: String,
        val fechaRegistro: String,
        val latitud: String,
        val longitud: String,
        val comment: String,
    )

}