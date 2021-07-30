package com.tawa.allinapp.features.init.usecase

import com.tawa.allinapp.core.interactor.UseCase
import com.tawa.allinapp.data.repository.CheckRepository
import javax.inject.Inject

class SyncCheck
@Inject constructor(private val checkRepository: CheckRepository) : UseCase<Boolean, UseCase.None>() {

    override suspend fun run(params: None) = checkRepository.syncChecks()

}