package com.tawa.allinapp.features.init.usecase

import com.tawa.allinapp.core.interactor.UseCase
import com.tawa.allinapp.data.repository.ParametersRepository
import com.tawa.allinapp.models.Company
import javax.inject.Inject

class GetCompanies
@Inject constructor(private val parametersRepository: ParametersRepository) : UseCase<List<Company>, UseCase.None>() {

    override suspend fun run(params: None) = parametersRepository.getCompanies()

}