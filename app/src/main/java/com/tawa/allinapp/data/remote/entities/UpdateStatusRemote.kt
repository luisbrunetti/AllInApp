package com.tawa.allinapp.data.remote.entities

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class UpdateStatusRemote {
    data class Request(
        @SerializedName("ul_latitud") @Expose val latitude: String,
        @SerializedName("ul_longitud") @Expose val longitude: String,
        @SerializedName("bateria") @Expose val battery: String
    )

    data class Response(
        @SerializedName("ul_latitud") @Expose val token: String,
    )
}