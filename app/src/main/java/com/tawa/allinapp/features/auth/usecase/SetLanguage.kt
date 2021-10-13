package com.tawa.allinapp.features.auth.usecase

import com.tawa.allinapp.core.interactor.UseCase
import com.tawa.allinapp.data.repository.CheckRepository
import com.tawa.allinapp.data.repository.ParametersRepository
import com.tawa.allinapp.features.init.usecase.SetIdPv
import javax.inject.Inject

class SetLanguage
@Inject constructor(private val paramsRepository: ParametersRepository) : UseCase<Boolean, SetLanguage.Params>() {

    override suspend fun run(params: Params) = paramsRepository.setLanguage(params.language)

    data class Params(val language:String)

}