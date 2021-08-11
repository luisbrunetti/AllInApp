package com.tawa.allinapp.data.local.datasource

import com.tawa.allinapp.data.local.dao.ParametersDao
import com.tawa.allinapp.data.local.dao.ReportsDao
import com.tawa.allinapp.data.local.models.*
import com.tawa.allinapp.features.init.usecase.GetIdPV
import com.tawa.allinapp.models.Sku
import javax.inject.Inject


class ReportsDataSource @Inject constructor(private val reportsDao: ReportsDao){
    fun insertReports(reportModel: ReportModel){
        reportsDao.insertReports(reportModel)
    }

    fun getReports():List<ReportModel> {
        return reportsDao.getReports()
    }

    fun listReports(idCompany:String):List<ReportModel> {
        return reportsDao.listReports(idCompany)
    }

    fun updateStateReports(idReport:String,state:String)
    {
        reportsDao.updateStateReports(idReport,state)
    }

    fun updateStateSku(idSku:String,state:String)
    {
        reportsDao.updateStateSku(idSku,state)
    }

    fun insertPhotoReport(reportModel: PhotoReportModel){
        reportsDao.insertPhotoReport(reportModel)
    }

    fun getPhotoReports(pv:String, company:String):List<PhotoReportModel> {
        return reportsDao.getPhotoReports(pv,company)
    }

    fun getAllPhotoReports():List<PhotoReportModel> {
        return reportsDao.getAllPhotoReports()
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

    fun getSku(idPV: String):List<SkuModel>{
        return reportsDao.getSku(idPV)
    }

    fun getStateSku(idPV: String):String{
        return reportsDao.getStateSku(idPV)
    }

    fun getStatePhoto(pv: String):String?{
        return reportsDao.getStatePhoto(pv)
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