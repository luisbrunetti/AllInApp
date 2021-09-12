package com.tawa.allinapp.models

import com.tawa.allinapp.data.local.models.AudioReportModel

data class AudioReport (
    val idPv: String,
    val idUser:String,
    val selected:String,
    val selectedName: String,
    val record:String,
    val recordPath: String,
    val recordSent: String,
    val comments:String,
){
    fun toModel(): AudioReportModel {
        return AudioReportModel(
            idPv = this.idPv,
            idUser = this.idUser,
            selected = this.selected,
            selectedName = this.selectedName,
            record = this.record,
            recordPath = this.recordPath,
            recordSent = this.recordSent,
            comments = this.comments
        )
    }
}