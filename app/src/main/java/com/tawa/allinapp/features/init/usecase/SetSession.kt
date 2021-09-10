package com.tawa.allinapp.features.init.usecase

import com.tawa.allinapp.core.interactor.UseCase
import com.tawa.allinapp.data.repository.ReportsRepository
import javax.inject.Inject

class SetSession
@Inject constructor(private val reportsRepository: ReportsRepository) : UseCase<Boolean, SetSession.Params>() {

    override suspend fun run(params: Params) = reportsRepository.setSession(params.value)
    data class Params(val value: Boolean)

}