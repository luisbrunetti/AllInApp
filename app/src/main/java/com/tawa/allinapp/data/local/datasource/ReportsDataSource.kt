package com.tawa.allinapp.data.local.datasource

import com.tawa.allinapp.data.local.dao.ParametersDao
import com.tawa.allinapp.data.local.dao.ReportsDao
import com.tawa.allinapp.data.local.models.*
import javax.inject.Inject


class ReportsDataSource @Inject constructor(private val reportsDao: ReportsDao){
    fun insertReports(reportModel: ReportModel){
        reportsDao.insertReports(reportModel)
    }

    fun getReports():List<ReportModel> {
        return reportsDao.getReports()
    }

    fun insertPhotoReport(reportModel: PhotoReportModel){
        reportsDao.insertPhotoReport(reportModel)
    }

    fun getPhotoReports():List<PhotoReportModel> {
        return reportsDao.getPhotoReports()
    }

}