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

    fun getReports(idCompany: String):List<ReportModel> {
        return reportsDao.getReports(idCompany)
    }

    fun getReportsPv(idCompany: String,idPV: String):List<ReportModel> {
        return reportsDao.getReportsPv(idCompany,idPV)
    }

    fun getStateReport(idReport: String,idPV: String):String {
        return reportsDao.getStateReport(idReport,idPV)
    }

    fun listReports(idCompany:String):List<ReportModel> {
        return reportsDao.listReports(idCompany)
    }

    fun updateStateReports(idReport:String,state:String,type:String,idPV: String)
    {
        reportsDao.updateStateReports(idReport,state,type,idPV)
    }

    fun updateStateSku(idSku:String,state:String,type: String)
    {
        reportsDao.updateStateSku(idSku,state,type)
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

    fun insertReportPv(reportPvModel: ReportPvModel){
            reportsDao.insertReportPv(reportPvModel)
    }

    fun reportPvCount(idReport: String,idPV: String):Int{
        return reportsDao.reportPvCount(idReport,idPV)
    }

    fun getReportPvCount(idCompany: String,idPV: String):Int{
        return reportsDao.getReportPvCount(idCompany,idPV)
    }

    fun updateReportPv(idReport: String,idPV: String,state: String,type: String){
        reportsDao.updateReportPv(idReport,idPV,state,type)
    }

}