package com.tawa.allinapp.features.coverage

import com.tawa.allinapp.core.interactor.UseCase
import com.tawa.allinapp.data.repository.DashboardRepository
import com.tawa.allinapp.models.Channel
import javax.inject.Inject

class GetChannels
@Inject constructor(private val dashboardRepository: DashboardRepository) : UseCase<List<Channel>, UseCase.None>() {

    override suspend fun run(params:None) = dashboardRepository.getChannels()

}
