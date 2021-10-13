package com.tawa.allinapp.features.init.usecase

import com.tawa.allinapp.core.interactor.UseCase
import com.tawa.allinapp.data.repository.ParametersRepository
import com.tawa.allinapp.models.Language
import javax.inject.Inject

class GetLanguageByXml
@Inject constructor(private val parametersRepository: ParametersRepository) : UseCase<List<Language>, GetLanguageByXml.Params>() {

    override suspend fun run(params: Params) = parametersRepository.getLanguageByXml(params.xmlName)
    data class Params(val xmlName:String)

}