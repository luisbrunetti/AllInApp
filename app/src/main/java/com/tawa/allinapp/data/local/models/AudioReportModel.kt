package com.tawa.allinapp.data.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reports_audio")
data class AudioReportModel(
    @PrimaryKey(autoGenerate = true) val id: Int=0,
    val selected: String,
    val record: String,
    val comments: String?,
)