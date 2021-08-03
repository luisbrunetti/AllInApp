package com.tawa.allinapp.data.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.tawa.allinapp.data.remote.entities.ReportsPhotoRemote


@Entity(tableName = "reports_photo")
data class PhotoReportModel(
    @PrimaryKey (autoGenerate = true) val id: Int=0,
    val company: String?,
    val pv: String?,
    val before1: String?="",
    val before2: String?="",
    val before3: String?="",
    val before4: String?="",
    val before5: String?="",
    val after1: String?="",
    val after2: String?="",
    val after3: String?="",
    val after4: String?="",
    val after5: String?="",
    val comments: String?,
    val createAt: String?,
){
    fun toRemote() = ReportsPhotoRemote.Request(company, pv, before1, before2, before3, before4, before5, after1, after2, after3, after4, after5, comments, createAt,)
}