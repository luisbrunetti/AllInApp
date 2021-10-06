package com.tawa.allinapp.features.coverage.usecase

import com.tawa.allinapp.core.interactor.UseCase
import com.tawa.allinapp.data.repository.DashboardRepository
import com.tawa.allinapp.models.Chain
import javax.inject.Inject


class GetChains
@Inject constructor(private val dashboardRepository: DashboardRepository) : UseCase<List<Chain>, GetChains.Params>() {

    override suspend fun run(params: Params) = dashboardRepository.getChains(params.channel,params.retail)

    data class Params(val channel: List<String>, val retail: List<String>)

}