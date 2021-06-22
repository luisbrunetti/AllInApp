package com.tawa.allinapp.features.init.usecase

import com.tawa.allinapp.core.interactor.UseCase
import com.tawa.allinapp.data.repository.CheckRepository
import com.tawa.allinapp.data.repository.ParametersRepository
import com.tawa.allinapp.models.PV
import javax.inject.Inject

class SetIdCompany
@Inject constructor(private val checkRepository: CheckRepository) : UseCase<Boolean, SetIdCompany.Params>() {

    override suspend fun run(params: Params) = checkRepository.setIdCompany(params.idCompany)

    data class Params(val idCompany: String)

}