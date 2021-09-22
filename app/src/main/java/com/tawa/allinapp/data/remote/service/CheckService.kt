package com.tawa.allinapp.data.remote.service

import com.tawa.allinapp.data.remote.api.CheckApi
import com.tawa.allinapp.data.remote.entities.CheckRemote
import retrofit2.Retrofit
import retrofit2.http.Header
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CheckService
@Inject constructor(retrofit: Retrofit) : CheckApi {
    private val checkApi by lazy { retrofit.create(CheckApi::class.java) }

    override fun sendCheckIn(token:String,request: CheckRemote.Check) = checkApi.sendCheckIn(token,request)
    override fun sendCheckOut(token: String,request: CheckRemote.Check) = checkApi.sendCheckOut(token,request)
    override fun syncChecks(token: String, request:List<CheckRemote.Request>) = checkApi.syncChecks(token,request)

}