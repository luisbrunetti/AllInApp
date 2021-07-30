package com.tawa.allinapp.models

import com.tawa.allinapp.data.local.models.PhotoReportModel
import java.util.*

data class PhotoReport (
    val company:String,
    val pv:String,
    val before:List<String?>,
    val after:List<String?>,
    val comments:String,
    val createAt:String
){
    fun toModel(): PhotoReportModel {
        return PhotoReportModel(
            0,
            company,
            pv,
            before[0],
            before[1],
            before[2],
            before[3],
            before[4],
            after[0],
            after[1],
            after[2],
            after[3],
            after[4],
            comments,
            createAt
        )
    }
}