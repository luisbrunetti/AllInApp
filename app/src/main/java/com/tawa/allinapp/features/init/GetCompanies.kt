package com.tawa.allinapp.features.init

import com.tawa.allinapp.core.functional.Either
import com.tawa.allinapp.core.functional.Failure
import com.tawa.allinapp.core.interactor.UseCase
import com.tawa.allinapp.data.local.models.CompanyModel
import com.tawa.allinapp.data.repository.ParametersRepository
import com.tawa.allinapp.features.auth.Company
import javax.inject.Inject

class GetCompanies
@Inject constructor(private val parametersRepository: ParametersRepository) : UseCase<List<Company>, UseCase.None>() {

    override suspend fun run(params: None) = parametersRepository.getCompanies()

}