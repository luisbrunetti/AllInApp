package com.tawa.allinapp.features.coverage

import com.tawa.allinapp.core.interactor.UseCase
import com.tawa.allinapp.data.repository.DashboardRepository
import com.tawa.allinapp.models.User
import javax.inject.Inject

class GetUserList
@Inject constructor(private val dashboardRepository: DashboardRepository) : UseCase<List<User>, UseCase.None>() {

    override suspend fun run(params: None) = dashboardRepository.getUserList()

}
