package com.tawa.allinapp.models

import com.tawa.allinapp.data.local.models.AudioReportModel

data class AudioReport (
    val idPv: String,
    val idUser:String,
    val selected:String,
    val selectedName: String,
    val selectedPath: String,
    val record:String,
    val recordPath: String,
    val recordSent: String
){
    fun toModel(): AudioReportModel {
        return AudioReportModel(
            idPv = this.idPv,
            idUser = this.idUser,
            selected = this.selected,
            selectedName = this.selectedName,
            selectedPath = this.selectedPath,
            record = this.record,
            recordPath = this.recordPath,
            recordSent = this.recordSent
        )
    }
}