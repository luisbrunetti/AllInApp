package com.tawa.allinapp.features.coverage

import com.tawa.allinapp.core.interactor.UseCase
import com.tawa.allinapp.data.repository.DashboardRepository
import com.tawa.allinapp.models.Chain
import javax.inject.Inject


class GetChains
@Inject constructor(private val dashboardRepository: DashboardRepository) : UseCase<List<Chain>, UseCase.None>() {

    override suspend fun run(params: None) = dashboardRepository.getChains()

}