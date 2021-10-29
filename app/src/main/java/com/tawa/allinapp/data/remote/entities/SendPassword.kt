package com.tawa.allinapp.data.remote.entities

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SendPassword {
    data class Request(
        @SerializedName("usuario") @Expose val user: String
    )

    data class Response(
        @SerializedName("message") @Expose val message: String,
    )
}