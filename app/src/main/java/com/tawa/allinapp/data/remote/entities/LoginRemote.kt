package com.tawa.allinapp.data.remote.entities

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class LoginRemote {
    data class Request(
        @SerializedName("usuario") @Expose val userName: String,
        @SerializedName("contraseña") @Expose val password: String
    )

    data class Response(
        @SerializedName("access_token") @Expose val token: String,
        @SerializedName("usuario") @Expose val user: String,
        @SerializedName("id_user") @Expose val idUser: String,
        @SerializedName("roles") @Expose val role: List<String>,
        @SerializedName("nombre_apellido") @Expose val fullName: String,
        @SerializedName("contraseña") @Expose val psw: String,
    )
}