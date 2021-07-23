package com.tawa.allinapp.data.remote.api

import com.tawa.allinapp.core.platform.BaseResponse
import com.tawa.allinapp.data.remote.entities.LoginRemote
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("http://192.168.1.44:3000/api/user/login/") //good
    //@POST("https://run.mocky.io/v3/f486753a-8f9a-4dc3-b1e0-5030c2de3c34") //error
    fun login(@Body request: LoginRemote.Request): Call<BaseResponse<LoginRemote.Response>>

}