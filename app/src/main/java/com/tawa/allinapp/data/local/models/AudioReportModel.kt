package com.tawa.allinapp.data.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.tawa.allinapp.models.AudioReport

@Entity(tableName = "reports_audio")
data class AudioReportModel(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val idPv: String,
    val idUser: String,
    val selected: String,
    val selectedName: String,
    val record: String,
    val recordPath: String,
    val recordSent: String,
    val comments: String?,
) {
    fun toView() = AudioReport(
        idPv = idPv,
        idUser=idUser,
        selected = selected,
        selectedName = selectedName,
        record = record,
        recordPath = recordPath,
        recordSent = recordSent,
        comments = comments!!
    )
}