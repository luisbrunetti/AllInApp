package com.tawa.allinapp.features.coverage

import com.tawa.allinapp.core.interactor.UseCase
import com.tawa.allinapp.data.repository.DashboardRepository
import com.tawa.allinapp.models.CoverageGraph
import javax.inject.Inject

class GetGraph
@Inject constructor(private val dashboardRepository: DashboardRepository) : UseCase<CoverageGraph, GetGraph.Params>() {

    override suspend fun run(params: Params) = dashboardRepository.getGraph(params.start,params.end,params.users,params.chains)

    data class Params(val start:String?, val end:String?, val users: List<String>?, val chains: List<String>?)
}