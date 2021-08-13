package com.tawa.allinapp.data.remote.service

import com.tawa.allinapp.data.remote.api.DashboardApi
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DashboardService
@Inject constructor(retrofit: Retrofit) : DashboardApi {
    private val dashboardApi by lazy { retrofit.create(DashboardApi::class.java) }

    override fun getChannels(token: String,company:String) = dashboardApi.getChannels(token,company)

}