package com.tawa.allinapp.data.remote.service


import com.tawa.allinapp.core.platform.BaseResponse
import com.tawa.allinapp.data.remote.api.PdvApi
import com.tawa.allinapp.data.remote.entities.PdvRemote
import retrofit2.Call
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class PdvService
@Inject constructor(retrofit: Retrofit) : PdvApi {
    private val pdvApi by lazy { retrofit.create(PdvApi::class.java) }

    override fun getPdvCompany(token: String,idCompany: String) = pdvApi.getPdvCompany(token,idCompany)

    override fun updatePdvRemote(token: String, idPdv: String, request: PdvRemote.Request) = pdvApi.updatePdvRemote(token,idPdv,request)

}