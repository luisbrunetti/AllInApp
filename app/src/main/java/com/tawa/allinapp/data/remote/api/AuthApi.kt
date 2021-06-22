package com.tawa.allinapp.data.remote.api

import com.tawa.allinapp.core.platform.BaseResponse
import com.tawa.allinapp.data.remote.entities.LoginRemote
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("https://run.mocky.io/v3/5667eb38-5bc1-4da2-9db1-172a3b6d61e8") //good
    //@POST("https://run.mocky.io/v3/f486753a-8f9a-4dc3-b1e0-5030c2de3c34") //error
    fun login(@Body request: LoginRemote.Request): Call<BaseResponse<LoginRemote.Response>>

}