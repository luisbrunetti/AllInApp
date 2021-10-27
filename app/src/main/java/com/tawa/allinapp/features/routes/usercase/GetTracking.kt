package com.tawa.allinapp.features.routes.usercase

import com.tawa.allinapp.core.interactor.UseCase
import com.tawa.allinapp.data.repository.ReportsRepository
import com.tawa.allinapp.data.repository.RoutesRepository
import com.tawa.allinapp.models.Report
import com.tawa.allinapp.models.Routes
import com.tawa.allinapp.models.RoutesUser
import com.tawa.allinapp.models.Tracking
import javax.inject.Inject


class GetTracking
@Inject constructor(private val routesRepository: RoutesRepository) : UseCase<List<Tracking>, GetTracking.Params>() {

    override suspend fun run(params: Params) = routesRepository.getTracking(params.idUser,params.dateStart,params.type)
    data class Params(val idUser:String,val dateStart:String,val type:Int)

}