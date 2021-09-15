package com.tawa.allinapp.features.reports.geolocation.usecase

import com.tawa.allinapp.core.interactor.UseCase
import com.tawa.allinapp.data.repository.RoutesRepository
import com.tawa.allinapp.models.RoutesUser
import javax.inject.Inject

class GetListUserGeolocation
@Inject constructor(private val routesRepository: RoutesRepository) : UseCase<List<RoutesUser>, UseCase.None>() {
    override suspend fun run(params: None) = routesRepository.getListUserRemote()
}

