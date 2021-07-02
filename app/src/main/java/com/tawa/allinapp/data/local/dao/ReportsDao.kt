package com.tawa.allinapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tawa.allinapp.data.local.models.QuestionModel
import com.tawa.allinapp.data.local.models.ReportModel
import com.tawa.allinapp.models.Question

@Dao
interface ReportsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertReports(reportModel: ReportModel)

    @Query("SELECT * FROM reports")
    fun getReports(): List<ReportModel>




}