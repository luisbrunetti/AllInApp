package com.tawa.allinapp.features.coverage

import com.tawa.allinapp.core.interactor.UseCase
import com.tawa.allinapp.data.repository.DashboardRepository
import com.tawa.allinapp.models.Retail
import javax.inject.Inject

class GetRetails
@Inject constructor(private val dashboardRepository: DashboardRepository) : UseCase<List<Retail>, UseCase.None>() {

    override suspend fun run(params: None) = dashboardRepository.getRetails()

}
