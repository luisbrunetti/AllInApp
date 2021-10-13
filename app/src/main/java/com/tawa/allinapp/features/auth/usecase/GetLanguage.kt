package com.tawa.allinapp.features.auth.usecase

import com.tawa.allinapp.core.interactor.UseCase
import com.tawa.allinapp.data.repository.ParametersRepository
import com.tawa.allinapp.data.repository.QuestionsRepository
import javax.inject.Inject

class GetLanguage
@Inject constructor(private val parametersRepository: ParametersRepository) : UseCase<String, UseCase.None>() {

    override suspend fun run(params: None) = parametersRepository.getLanguage()

}