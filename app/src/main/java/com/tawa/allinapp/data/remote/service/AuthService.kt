package com.tawa.allinapp.data.remote.service

import com.tawa.allinapp.data.remote.api.AuthApi
import com.tawa.allinapp.data.remote.entities.LoginRemote
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthService
@Inject constructor(retrofit: Retrofit) : AuthApi {
    private val authApi by lazy { retrofit.create(AuthApi::class.java) }

    override fun login(request: LoginRemote.Request) = authApi.login(request)

}