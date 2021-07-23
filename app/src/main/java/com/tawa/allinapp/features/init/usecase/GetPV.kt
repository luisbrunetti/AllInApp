package com.tawa.allinapp.features.init.usecase

import com.tawa.allinapp.core.interactor.UseCase
import com.tawa.allinapp.data.repository.ParametersRepository
import com.tawa.allinapp.models.Schedule
import javax.inject.Inject

class GetPV
@Inject constructor(private val parametersRepository: ParametersRepository) : UseCase<List<Schedule>, GetPV.Params>() {

    override suspend fun run(params: Params) = parametersRepository.getPV(params.company)

    data class Params(val company: String)

}