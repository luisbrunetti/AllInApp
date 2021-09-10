package com.tawa.allinapp.models

import com.tawa.allinapp.data.local.models.AudioReportModel

data class AudioReport (
    val selected:String,
    val selectedPath: String,
    val record:String,
    val recordPath: String,
    val comments:String,
){
    fun toModel(): AudioReportModel {
        return AudioReportModel(
            selected = this.selected,
            selectedPath = this.selectedPath,
            record = this.record,
            recordPath = this.recordPath,
            comments = this.comments
        )
    }
}