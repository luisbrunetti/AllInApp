package com.tawa.allinapp.features.splash

import com.tawa.allinapp.core.interactor.UseCase
import com.tawa.allinapp.data.repository.AuthRepository
import com.tawa.allinapp.data.repository.CheckRepository
import javax.inject.Inject

class GetIdCompany
@Inject constructor(private val checkRepository: CheckRepository) : UseCase<String, UseCase.None>() {

    override suspend fun run(params: None) = checkRepository.getIdCompany()

}