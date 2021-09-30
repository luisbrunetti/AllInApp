package com.tawa.allinapp.data.remote.service


import com.tawa.allinapp.core.platform.BaseResponse
import com.tawa.allinapp.data.remote.api.NotifyApi
import com.tawa.allinapp.data.remote.api.PdvApi
import com.tawa.allinapp.data.remote.entities.PdvRemote
import retrofit2.Call
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class NotifyService
@Inject constructor(retrofit: Retrofit) : NotifyApi {
    private val notifyApi by lazy { retrofit.create(NotifyApi::class.java) }

    override fun getNotify(token: String) = notifyApi.getNotify(token)
}