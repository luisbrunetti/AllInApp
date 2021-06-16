package com.tawa.allinapp.features.auth.usecase

import com.tawa.allinapp.core.interactor.UseCase
import com.tawa.allinapp.data.repository.ParametersRepository
import javax.inject.Inject


class GetPVRemote
@Inject constructor(private val parametersRepository: ParametersRepository) : UseCase<Boolean, UseCase.None>() {

    override suspend fun run(params: None) = parametersRepository.getPV()

}