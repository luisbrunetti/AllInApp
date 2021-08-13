package com.tawa.allinapp.features.coverage

import com.tawa.allinapp.core.interactor.UseCase
import com.tawa.allinapp.data.repository.DashboardRepository
import com.tawa.allinapp.models.Channel
import javax.inject.Inject

class GetChannels
@Inject constructor(private val dashboardRepository: DashboardRepository) : UseCase<List<Channel>, GetChannels.Params>() {

    override suspend fun run(params: Params) = dashboardRepository.getChannels(params.company)

    class Params(val company: String)

}
