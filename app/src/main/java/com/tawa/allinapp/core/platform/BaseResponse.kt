package com.tawa.allinapp.core.platform

import com.google.gson.annotations.SerializedName

data class BaseResponse<T>(@SerializedName("codigoError",alternate = ["CodigoError"]) val errorCode: String?,
                             @SerializedName("data",alternate = ["Data"]) val data: T,
                             @SerializedName("isSuccess",alternate = ["IsSuccess"]) val isSuccess: Boolean,
                             @SerializedName("message",alternate = ["Message"]) val message: String?)