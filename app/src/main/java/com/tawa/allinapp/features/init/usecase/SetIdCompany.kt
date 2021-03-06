package com.tawa.allinapp.features.init.usecase

import com.tawa.allinapp.core.interactor.UseCase
import com.tawa.allinapp.data.repository.CheckRepository
import javax.inject.Inject

class SetIdCompany
@Inject constructor(private val checkRepository: CheckRepository) : UseCase<Boolean, SetIdCompany.Params>() {

    override suspend fun run(params: Params) = checkRepository.setIdCompany(params.idCompany,params.image)

    data class Params(val idCompany: String, val image:String)

}