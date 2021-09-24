package com.tawa.allinapp.data.local.datasource

import com.tawa.allinapp.data.local.dao.ParametersDao
import com.tawa.allinapp.data.local.dao.ReportsDao
import com.tawa.allinapp.data.local.models.*
import com.tawa.allinapp.features.init.usecase.GetIdPV
import com.tawa.allinapp.features.init.usecase.GetIdUser
import com.tawa.allinapp.models.ReportPv
import com.tawa.allinapp.models.Sku
import javax.inject.Inject


class ReportsDataSource @Inject constructor(private val reportsDao: ReportsDao){
    fun insertReports(reportModel: ReportModel){
        reportsDao.insertReports(reportModel)
    }

    fun getReports(idCompany: String,idUser: String):List<ReportModel> {
        return reportsDao.getReports(idCompany,idUser)
    }

    fun getReportsPv(idCompany: String,idPV: String,idUser: String):List<ReportModel> {
        return reportsDao.getReportsPv(idCompany,idPV,idUser)
    }

    fun getReportsPvReady(idUser: String):List<ReportModel> {
        return reportsDao.getReportsPvReady(idUser)
    }


    fun getReportsPvState(idReport: String,idPV: String,idUser: String):ReportPv {
        return reportsDao.getReportsPvState(idReport,idPV,idUser)
    }

    fun getReportPv(idCompany: String,idPV: String,idUser: String,idReport: String):ReportModel {
        return reportsDao.getReportPv(idCompany,idPV,idUser,idReport)
    }

    fun getStateReport(idReport: String,idPV: String,idUser: String):String {
        return reportsDao.getStateReport(idReport,idPV,idUser)
    }

    fun listReports(idCompany:String):List<ReportModel> {
        return reportsDao.listReports(idCompany)
    }

    fun updateStateReports(idReport:String,state:String,type:String,idPV: String)
    {
        reportsDao.updateStateReports(idReport,state,type,idPV)
    }

    fun updateStateSku(idSku:String,state:String,type: String,dateCreation:String,latitude: String,longitude: String)
    {
        reportsDao.updateStateSku(idSku,state,type,dateCreation,latitude,longitude)
    }

    fun updateStateSku(idSku:String,state:String,type: String)
    {
        reportsDao.updateStateSku(idSku,state,type)
    }

    fun insertPhotoReport(reportModel: PhotoReportModel){
        reportsDao.insertPhotoReport(reportModel)
    }

    fun getPhotoReports(pv:String, company:String,idUser: String):List<PhotoReportModel> {
        return reportsDao.getPhotoReports(pv,company,idUser)
    }

    fun getAllPhotoReports(idUser: String):List<PhotoReportModel> {
        return reportsDao.getAllPhotoReports(idUser)
    }

    fun updatePhotoReports(state: String,type: String) {
        reportsDao.updatePhotoReport(state,type)
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

    fun getSku(idSku: String,idPV: String,idCompany: String,idUser: String):List<SkuModel>{
        return reportsDao.getSku(idSku,idPV,idCompany,idUser)
    }

    fun getSku(idPV: String,idCompany: String,idUser: String):List<SkuModel>{
        return reportsDao.getSku(idPV,idCompany,idUser)
    }

    fun getSkuReady(idUser: String):List<SkuModel>{
        return reportsDao.getSkuReady(idUser)
    }

    fun getStateSku(idPV: String,idCompany: String,idUser: String):String{
        return reportsDao.getStateSku(idPV,idCompany,idUser)
    }

    fun getSTypeSku(idPV: String,idCompany: String,idUser: String):String{
        return reportsDao.getTypeSku(idPV,idCompany,idUser)
    }

    fun deletePhotos(){
        return reportsDao.deletePhotos()
    }

    fun getStatePhoto(idCompany: String,pv: String, idUser: String):String?{
        return reportsDao.getStatePhoto(idCompany,pv, idUser)
    }

    fun getTypePhoto(idCompany: String,pv: String, idUser: String):String?{
        return reportsDao.getTypePhoto(idCompany,pv, idUser)
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

    fun getReportPvCount(idCompany: String,idPV: String,idUser: String):Int{
        return reportsDao.getReportPvCount(idCompany,idPV,idUser)
    }

    fun updateReportPv(idReport: String,idPV: String,idUser: String,state: String,type: String,time:String,latitude:String,longitude:String){
        reportsDao.updateReportPv(idReport,idPV,idUser,state,type,time,latitude,longitude)
    }

    fun updateReportPvSync(idReport: String,idPV: String,state: String,type: String,idUser: String){
        reportsDao.updateReportPvSync(idReport,idPV,state,type,idUser)
    }

    fun getCountSku(idPV: String,idUser: String):Int{
        return reportsDao.getCountSku(idPV,idUser)
    }
    fun getAudioReports(idPv: String,idUser: String): AudioReportModel? {
        return reportsDao.getAudioReports(idPv = idPv, idUser = idUser)
    }
    fun updateAudioReport(idPv: String, idUser:String, selected: String, selectedName: String, selectedPath:String, record:String, recordPath: String, recordSent: String){
        reportsDao.updateAudioReport(idPv,idUser,selected,selectedName, selectedPath,record,recordPath,recordSent)
    }
}