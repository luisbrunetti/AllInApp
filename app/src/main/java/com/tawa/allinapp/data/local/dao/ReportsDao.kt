package com.tawa.allinapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tawa.allinapp.data.local.models.AudioReportModel
import com.tawa.allinapp.data.local.models.PhotoReportModel
import com.tawa.allinapp.data.local.models.ReportModel

@Dao
interface ReportsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertReports(reportModel: ReportModel)

    @Query("SELECT * FROM reports")
    fun getReports(): List<ReportModel>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertPhotoReport(report:PhotoReportModel)

    @Query("SELECT * FROM reports_photo")
    fun getPhotoReports(): List<PhotoReportModel>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAudioReport(report:AudioReportModel)
}