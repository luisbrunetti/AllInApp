package com.tawa.allinapp.data.remote.service


import com.tawa.allinapp.data.remote.api.ReportsApi
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ReportsService
@Inject constructor(retrofit: Retrofit) : ReportsApi {
    private val reportsApi by lazy { retrofit.create(ReportsApi::class.java) }

    override fun getReports() = reportsApi.getReports()

}