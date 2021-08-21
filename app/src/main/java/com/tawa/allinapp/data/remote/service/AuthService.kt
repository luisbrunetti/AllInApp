package com.tawa.allinapp.data.remote.service

import com.tawa.allinapp.core.platform.BaseResponse
import com.tawa.allinapp.data.remote.api.AuthApi
import com.tawa.allinapp.data.remote.entities.LoginRemote
import com.tawa.allinapp.data.remote.entities.SendPassword
import retrofit2.Call
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthService
@Inject constructor(retrofit: Retrofit) : AuthApi {
    private val authApi by lazy { retrofit.create(AuthApi::class.java) }

    override fun login(request: LoginRemote.Request) = authApi.login(request)

    override fun sendPassword(token:String,request: SendPassword.Request) =authApi.sendPassword(token,request)

}