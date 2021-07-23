package com.tawa.allinapp.data.remote.entities

import com.google.gson.annotations.SerializedName

class LoginRemote {
    data class Request(
        @SerializedName("usuario") val userName: String,
        @SerializedName("contraseña") val password: String
    )

    data class Response(
        @SerializedName("access_token") val token: String,
        @SerializedName("id_user") val idUser: String,
    )
}