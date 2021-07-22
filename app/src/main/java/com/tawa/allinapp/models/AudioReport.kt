package com.tawa.allinapp.models

import com.tawa.allinapp.data.local.models.AudioReportModel

data class AudioReport (
    val selected:String,
    val record:String,
    val comments:String,
){
    fun toModel(): AudioReportModel {
        return AudioReportModel(
            selected = this.selected,
            record = this.record,
            comments = this.comments
        )
    }
}