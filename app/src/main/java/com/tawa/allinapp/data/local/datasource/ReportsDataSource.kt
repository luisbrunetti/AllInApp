package com.tawa.allinapp.data.local.datasource

import com.tawa.allinapp.data.local.dao.ParametersDao
import com.tawa.allinapp.data.local.dao.ReportsDao
import com.tawa.allinapp.data.local.models.*
import com.tawa.allinapp.models.Sku
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

    fun insertAudioReport(reportModel: AudioReportModel){
        reportsDao.insertAudioReport(reportModel)
    }

    fun insertSku(skuModel: SkuModel){
        reportsDao.insertSku(skuModel)
    }

    fun insertSkuDetail(skuDetailModel: SkuDetailModel){
        reportsDao.insertSkuDetail((skuDetailModel))
    }

    fun getSkuDetail(idSku: String):List<SkuDetailModel>{
        return reportsDao.getSkuDetail(idSku)
    }

    fun getSku():List<SkuModel>{
        return reportsDao.getSku()
    }

    fun insertSkuObservation(skuObservationModel: SkuObservationModel){
        reportsDao.insertSkuObservation(skuObservationModel)
    }

    fun getSkuObservation(idSkuDetail:String):List<SkuObservationModel>{
        return reportsDao.getSkuObservation(idSkuDetail)
    }

    fun updateSkuDetail(idSkuDetail: String,stock:Boolean,exhi:Boolean,price:Float){
        reportsDao.updateSkuDetail(idSkuDetail,stock,exhi,price)
    }

}