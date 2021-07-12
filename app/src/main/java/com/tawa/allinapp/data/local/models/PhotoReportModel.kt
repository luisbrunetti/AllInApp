package com.tawa.allinapp.data.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "reports_photo")
data class PhotoReportModel(
    @PrimaryKey (autoGenerate = true) val id: Int=0,
    val before1: String?,
    val before2: String?,
    val before3: String?,
    val before4: String?,
    val before5: String?,
    val after1: String?,
    val after2: String?,
    val after3: String?,
    val after4: String?,
    val after5: String?,
    val comments: String?,
)