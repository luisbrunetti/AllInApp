package com.tawa.allinapp.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class QuickBloxUser(val id:String, val name:String) : Parcelable
