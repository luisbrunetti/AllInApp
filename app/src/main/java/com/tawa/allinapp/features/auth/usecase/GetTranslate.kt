package com.tawa.allinapp.features.auth.usecase


import com.tawa.allinapp.core.functional.Either
import com.tawa.allinapp.core.functional.Failure
import com.tawa.allinapp.core.interactor.UseCase
import com.tawa.allinapp.data.repository.ParametersRepository
import com.tawa.allinapp.models.Translate
import javax.inject.Inject

class GetTranslate
@Inject constructor(private val parametersRepository: ParametersRepository) : UseCase<Translate, UseCase.None>() {

    override suspend fun run(params: None) = parametersRepository.getTranslate()

}