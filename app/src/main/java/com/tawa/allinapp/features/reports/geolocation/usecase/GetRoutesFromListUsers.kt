package com.tawa.allinapp.features.reports.geolocation.usecase


import com.tawa.allinapp.core.functional.Either
import com.tawa.allinapp.core.functional.Failure
import com.tawa.allinapp.core.interactor.UseCase
import com.tawa.allinapp.data.repository.RoutesRepository
import com.tawa.allinapp.models.Routes
import com.tawa.allinapp.models.RoutesInform
import com.tawa.allinapp.models.RoutesUser
import javax.inject.Inject

class GetRoutesFromListUsers
@Inject constructor(private val routesRepository: RoutesRepository) : UseCase<List<RoutesInform>, GetRoutesFromListUsers.Params>() {

    override suspend fun run(params: Params): Either<Failure, List<RoutesInform>> = routesRepository.getRoutesFromListUsers(params.listUsers,params.dateStart)

    data class Params(val listUsers : MutableList<RoutesUser>, val dateStart: String)
}