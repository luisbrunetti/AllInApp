package com.tawa.allinapp.features.auth.usecase

import com.tawa.allinapp.core.interactor.UseCase
import com.tawa.allinapp.data.remote.entities.LanguageRemote
import com.tawa.allinapp.data.repository.ParametersRepository
import com.tawa.allinapp.models.Language
import javax.inject.Inject

class GetTranslate
@Inject constructor(private val parametersRepository: ParametersRepository) : UseCase<List<Language>, GetTranslate.Params>() {
    override suspend fun run(params: Params) = parametersRepository.getTranslate(params.language)

    data class Params(val language: String)
}