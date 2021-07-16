package com.tawa.allinapp.data.remote.service

import com.tawa.allinapp.core.platform.BaseResponse
import com.tawa.allinapp.data.remote.api.AuthApi
import com.tawa.allinapp.data.remote.api.ParametersApi
import com.tawa.allinapp.data.remote.entities.CompanyRemote
import com.tawa.allinapp.data.remote.entities.LoginRemote
import com.tawa.allinapp.data.remote.entities.PVRemote
import retrofit2.Call
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ParametersService
@Inject constructor(retrofit: Retrofit) : ParametersApi {
    private val authApi by lazy { retrofit.create(ParametersApi::class.java) }

    override fun getCompanies(user: String, token:String) = authApi.getCompanies(user,token)
    override fun getPV() = authApi.getPV()

}