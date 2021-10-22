package com.tawa.allinapp.data.remote.entities


import com.google.gson.annotations.SerializedName


data class TranslateRemote(
    @SerializedName("btCheckIn")
    val btCheckIn: List<String>,
    @SerializedName("btnLoginFragment")
    val btnLoginFragment: List<String>,
    @SerializedName("btnSendPassword")
    val btnSendPassword: List<String>,
    @SerializedName("cbRememberLoginFragment")
    val cbRememberLoginFragment: List<String>,
    @SerializedName("edForgotPassword")
    val edForgotPassword: List<String>,
    @SerializedName("tvCalendarInitFragment")
    val tvCalendarInitFragment: List<String>,
    @SerializedName("tvDocumentsInitFragment")
    val tvDocumentsInitFragment: List<String>,
    @SerializedName("tvHellowLoginFragment")
    val tvHellowLoginFragment: List<String>,
    @SerializedName("tvInfoLoginFragment")
    val tvInfoLoginFragment: List<String>,
    @SerializedName("tvInformsInitFragment")
    val tvInformsInitFragment: List<String>,
    @SerializedName("tvInputUserSendPassword")
    val tvInputUserSendPassword: List<String>,
    @SerializedName("tvMessageInitFragment")
    val tvMessageInitFragment: List<String>,
    @SerializedName("tvPvInitFragment")
    val tvPvInitFragment: List<String>,
    @SerializedName("tvReportInitFragment")
    val tvReportInitFragment: List<String>,
    @SerializedName("tvRoutesInitFragment")
    val tvRoutesInitFragment: List<String>,
    @SerializedName("tvSyncInitFragment")
    val tvSyncInitFragment: List<String>,
    @SerializedName("tvTaskInitFragment")
    val tvTaskInitFragment: List<String>,
    @SerializedName("tvTitleSendPassword")
    val tvTitleSendPassword: List<String>,
    @SerializedName("tvWelcomeInitFragment")
    val tvWelcomeInitFragment: List<String>,
    @SerializedName("txtInputPasswordLoginFragment")
    val txtInputPasswordLoginFragment: List<String>,
    @SerializedName("txtInputUserLoginFragment")
    val txtInputUserLoginFragment: List<String>,
    @SerializedName("txtIptUserSendPassword")
    val txtIptUserSendPassword: List<String>
)







