package com.tawa.allinapp.features.init.usecase

import com.tawa.allinapp.core.interactor.UseCase
import com.tawa.allinapp.data.repository.CheckRepository
import javax.inject.Inject

class GetLogoCompany
@Inject constructor(private val checkRepository: CheckRepository) : UseCase<String, UseCase.None>() {

    override suspend fun run(params: None) = checkRepository.getLogoCompany()

}