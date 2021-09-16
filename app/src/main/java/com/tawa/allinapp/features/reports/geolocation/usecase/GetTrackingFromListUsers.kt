package com.tawa.allinapp.features.reports.geolocation.usecase

import com.tawa.allinapp.core.interactor.UseCase
import com.tawa.allinapp.data.repository.RoutesRepository
import com.tawa.allinapp.features.routes.usercase.GetTracking
import com.tawa.allinapp.models.RoutesUser
import com.tawa.allinapp.models.Tracking
import com.tawa.allinapp.models.TrackingInform
import javax.inject.Inject

class GetTrackingFromListUsers
@Inject constructor(private val routesRepository: RoutesRepository) : UseCase<List<TrackingInform>, GetTrackingFromListUsers.Params>() {

    override suspend fun run(params: Params) = routesRepository.getTrackingFromListUsers(params.listUsers,params.dateStart)
    data class Params(val listUsers : ArrayList<RoutesUser>, val dateStart: String)

}