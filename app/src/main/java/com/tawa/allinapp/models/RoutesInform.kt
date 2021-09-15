package com.tawa.allinapp.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class RoutesInform(
    val idUser:String,
    val nameUser: String,
    val listRoutes: List<Routes>
)