package com.tawa.allinapp.data.remote.service


import com.tawa.allinapp.core.platform.BaseResponse
import com.tawa.allinapp.data.remote.api.QuestionsApi
import com.tawa.allinapp.data.remote.api.RoutesApi
import com.tawa.allinapp.data.remote.entities.QuestionsRemote
import com.tawa.allinapp.data.remote.entities.RoutesRemote
import com.tawa.allinapp.features.init.usecase.GetIdCompany
import retrofit2.Call
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class RoutesService
@Inject constructor(retrofit: Retrofit) : RoutesApi {
    private val routesApi by lazy { retrofit.create(RoutesApi::class.java) }

    override fun getListUser(token: String) = routesApi.getListUser(token)

    override fun getRoutes(token: String, idCompany: String, idUser: String, dateStart: String) = routesApi.getRoutes(token,idCompany,idUser,dateStart)
}