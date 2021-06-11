package com.tawa.allinapp.core.platform

import com.google.gson.annotations.SerializedName

data class BaseResponse<T>(@SerializedName("data") val data: T,
                           @SerializedName("success") val success: Boolean,
                           @SerializedName("message") val message: String?)