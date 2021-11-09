package com.tawa.allinapp.features.init.usecase


import com.tawa.allinapp.core.functional.Either
import com.tawa.allinapp.core.functional.Failure
import com.tawa.allinapp.core.interactor.UseCase
import com.tawa.allinapp.data.repository.ParametersRepository
import com.tawa.allinapp.models.Translate
import com.tawa.allinapp.models.TranslateItem
import javax.inject.Inject

class GetTranslate
@Inject constructor(private val parametersRepository: ParametersRepository) : UseCase<List<TranslateItem>, UseCase.None>() {

    override suspend fun run(params: None) = parametersRepository.getTranslate()

}