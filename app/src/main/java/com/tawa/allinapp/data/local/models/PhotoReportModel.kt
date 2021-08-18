package com.tawa.allinapp.data.local.models

import androidx.room.Entity
import com.tawa.allinapp.data.remote.entities.ReportsPhotoRemote
import com.tawa.allinapp.models.PhotoReport


@Entity(tableName = "reports_photo",primaryKeys = ["company","pv"])
data class PhotoReportModel(
    val company: String,
    val pv: String,
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
    val state: String?,
){
    fun toRemote() = ReportsPhotoRemote.Request(company, pv, before1, before2, before3, before4, before5, after1, after2, after3, after4, after5, comments, createAt,null,null,null,null,null) //TODO replace nulls
    fun toView():PhotoReport{
        val before = arrayListOf<String>()
        val after = arrayListOf<String>()
        if(before1!!.isNotEmpty()) before.add(before1)
        if(before2!!.isNotEmpty()) before.add(before2)
        if(before3!!.isNotEmpty()) before.add(before3)
        if(before4!!.isNotEmpty()) before.add(before4)
        if(before5!!.isNotEmpty()) before.add(before5)
        if(after1!!.isNotEmpty()) after.add(after1)
        if(after2!!.isNotEmpty()) after.add(after2)
        if(after3!!.isNotEmpty()) after.add(after3)
        if(after4!!.isNotEmpty()) after.add(after4)
        if(after5!!.isNotEmpty()) after.add(after5)
        return PhotoReport(before,after,comments?:"",createAt?:"")
    }
}