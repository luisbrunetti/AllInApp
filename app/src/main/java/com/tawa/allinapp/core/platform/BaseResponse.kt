package com.tawa.allinapp.core.platform

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class BaseResponse<T>(@SerializedName("data") @Expose val data: T,
                           @SerializedName("success") @Expose val success: Boolean,
                           @SerializedName("message") @Expose val message: String?)