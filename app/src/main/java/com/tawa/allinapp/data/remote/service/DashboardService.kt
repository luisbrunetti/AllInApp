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
    override fun getRetails(token: String,company:String) = dashboardApi.getRetails(token,company)
    override fun getChains(token: String,company:String,channel:List<String>,retail:List<String>) = dashboardApi.getChains(token,company,channel,retail)
    override fun getUserList(token: String,company:String) = dashboardApi.getUserList(token,company)
    override fun getGraph(token: String,company:String?,dateStart:String?,dateEnd:String?,user:List<String>?,chains:List<String>?) = dashboardApi.getGraph(token,company,dateStart,dateEnd,user,chains)
}