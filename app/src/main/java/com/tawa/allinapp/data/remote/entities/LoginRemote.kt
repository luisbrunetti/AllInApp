package com.tawa.allinapp.data.remote.entities

import com.google.gson.annotations.SerializedName

class LoginRemote {
    data class Request(
        @SerializedName("username") val userName: String,
        @SerializedName("password") val password: String
    )

    data class Response(@SerializedName("access_token") val token: String)
}