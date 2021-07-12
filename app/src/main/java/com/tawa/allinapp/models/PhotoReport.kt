package com.tawa.allinapp.models

import com.tawa.allinapp.data.local.models.PhotoReportModel

data class PhotoReport (
    val before:List<String?>,
    val after:List<String?>,
    val comments:String,
){
    fun toModel(): PhotoReportModel {
        return PhotoReportModel(
            before1 = this.before[0],
            before2 = this.before[1],
            before3 = this.before[2],
            before4 = this.before[3],
            before5 = this.before[4],
            after1 = this.after[0],
            after2 = this.after[1],
            after3 = this.after[2],
            after4 = this.after[3],
            after5 = this.after[4],
            comments = this.comments
        )
    }

}