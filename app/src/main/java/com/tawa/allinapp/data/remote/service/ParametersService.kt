package com.tawa.allinapp.data.remote.service

import com.tawa.allinapp.core.platform.BaseResponse
import com.tawa.allinapp.data.remote.api.ParametersApi
import com.tawa.allinapp.models.Translate
import retrofit2.Call
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ParametersService
@Inject constructor(retrofit: Retrofit) : ParametersApi {
    private val authApi by lazy { retrofit.create(ParametersApi::class.java) }

    override fun getCompanies(token:String) = authApi.getCompanies(token)
    override fun getSchedule(token:String, company:String) = authApi.getSchedule(token,company)
    override fun getLanguage(): Call<BaseResponse<Translate>> = authApi.getLanguage()
}