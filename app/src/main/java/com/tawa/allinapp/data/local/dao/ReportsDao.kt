package com.tawa.allinapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tawa.allinapp.data.local.models.*
import com.tawa.allinapp.features.init.usecase.GetIdCompany
import com.tawa.allinapp.features.init.usecase.GetIdUser
import com.tawa.allinapp.models.ReportPv
import com.tawa.allinapp.models.Sku

@Dao
interface ReportsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertReports(reportModel: ReportModel)

    @Query("SELECT * FROM reports WHERE idCompany=:idCompany")
    fun getReports(idCompany: String): List<ReportModel>

    @Query("SELECT * FROM report_pv WHERE idReport=:idReport and idPv=:idPv and idUser=:idUser")
    fun getReportsPvState(idReport: String,idPv: String,idUser: String): ReportPv

    @Query("SELECT reports.id,reports.reportName,reports.idCompany,reports.nameCompany,reports.idUser,reports.idUserMod,reports.feMod,reports.feCreate,report_pv.state,report_pv.type,report_pv.idPv FROM reports,report_pv WHERE reports.id=report_pv.idReport and  reports.idCompany=:idCompany and report_pv.idPv=:idPv and report_pv.idUser=:idUser")
    fun getReportsPv(idCompany: String,idPv: String,idUser: String): List<ReportModel>

    @Query("SELECT reports.id,reports.reportName,reports.idCompany,reports.nameCompany,reports.idUser,reports.idUserMod,reports.feMod,reports.feCreate,report_pv.state,report_pv.type,report_pv.idPv FROM reports,report_pv WHERE reports.id=report_pv.idReport and  reports.idCompany=:idCompany and report_pv.idPv=:idPv and report_pv.idUser=:idUser and report_pv.type='Terminado'")
    fun getReportsPvReady(idCompany: String,idPv: String,idUser: String): List<ReportModel>

    @Query("SELECT reports.id,reports.reportName,reports.idCompany,reports.nameCompany,reports.idUser,reports.idUserMod,reports.feMod,reports.feCreate,report_pv.state,report_pv.type,report_pv.idPv FROM reports,report_pv WHERE reports.id=report_pv.idReport and  reports.idCompany=:idCompany and report_pv.idPv=:idPv and report_pv.idUser=:idUser and reports.id=:idReport")
    fun getReportPv(idCompany: String,idPv: String,idUser: String,idReport: String): ReportModel

    @Query("SELECT report_pv.type FROM reports,report_pv WHERE reports.id=report_pv.idReport and  reports.id=:idReport and report_pv.idPv=:idPv and report_pv.idUser=:idUser")
    fun getStateReport(idReport: String,idPv: String,idUser: String): String

    @Query("SELECT * FROM reports where idCompany=:idCompany")
    fun listReports(idCompany: String): List<ReportModel>

    @Query("UPDATE reports set state=:state, type=:type,idPv=:idPv WHERE id=:idReport")
    fun updateStateReports(idReport:String,state:String,type:String,idPv: String)

    @Query("UPDATE sku set state=:state, type=:type,dateCreation=:dateCreation, latitude=:latitude,longitude=:longitude WHERE id=:idSku")
    fun updateStateSku(idSku:String,state:String,type: String,dateCreation:String,latitude: String,longitude: String)

    @Query("UPDATE sku set state=:state, type=:type WHERE id=:idSku")
    fun updateStateSku(idSku:String,state:String,type: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPhotoReport(report:PhotoReportModel)

    @Query("SELECT * FROM reports_photo WHERE pv=:pv AND company=:company")
    fun getPhotoReports(pv:String, company:String): List<PhotoReportModel>

    @Query("SELECT * FROM reports_photo")
    fun getAllPhotoReports(): List<PhotoReportModel>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAudioReport(report:AudioReportModel)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertSku(skuModel: SkuModel)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertSkuDetail(skuDetailModel: SkuDetailModel)

    @Query("SELECT * FROM sku_detail WHERE idSku=:idSku")
    fun getSkuDetail(idSku:String): List<SkuDetailModel>

    @Query("SELECT * FROM sku WHERE idPv=:idPv and idCompany=:idCompany and idUser=:idUser and id =:idSku")
    fun getSku(idSku: String,idPv:String,idCompany: String,idUser: String): List<SkuModel>

    @Query("SELECT * FROM sku WHERE idPv=:idPv and idCompany=:idCompany and idUser=:idUser")
    fun getSku(idPv:String,idCompany: String,idUser: String): List<SkuModel>

    @Query("SELECT * FROM sku WHERE idPv=:idPv and idCompany=:idCompany and idUser=:idUser and type='Terminado'")
    fun getSkuReady(idPv:String,idCompany: String,idUser: String): List<SkuModel>

    @Query("SELECT state FROM sku WHERE idPv=:idPv and idCompany=:idCompany and idUser=:idUser")
    fun getStateSku(idPv:String,idCompany: String,idUser: String): String

    @Query("SELECT type FROM sku WHERE idPv=:idPv and idCompany=:idCompany and idUser=:idUser")
    fun getTypeSku(idPv:String,idCompany: String,idUser: String): String

    @Query("SELECT state FROM reports_photo WHERE pv=:pv")
    fun getStatePhoto(pv:String): String?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertSkuObservation(skuObservationModel: SkuObservationModel)

    @Query("SELECT * FROM sku_observation where idSkuDetail = :idSkuDetail")
    fun getSkuObservation(idSkuDetail:String):List<SkuObservationModel>

    @Query("UPDATE sku_detail set stock=:stock , exhibition=:exhi,newPrice=:price WHERE id=:idSkuDetail")
    fun updateSkuDetail(idSkuDetail:String,stock:Boolean,exhi:Boolean,price:Float)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertReportPv(reportPvModel: ReportPvModel)

    @Query("SELECT count(id) FROM report_pv WHERE idReport = :idReport and idPv = :idPv   ")
    fun reportPvCount(idReport: String,idPv: String):Int

    @Query("SELECT count(reports.id) FROM reports,report_pv WHERE reports.id=report_pv.idReport and  reports.idCompany=:idCompany and report_pv.idPv=:idPv and report_pv.idUser=:idUser")
    fun getReportPvCount(idCompany: String,idPv: String,idUser: String):Int

    @Query("UPDATE report_pv SET state=:state , type = :type, time=:time,latitude=:latitude,longitude=:longitude WHERE idReport = :idReport and idPv = :idPv")
    fun updateReportPv(idReport: String,idPv: String,state: String,type:String,time:String,latitude:String,longitude:String)

    @Query("UPDATE report_pv SET state=:state , type = :type WHERE idReport = :idReport and idPv = :idPv")
    fun updateReportPvSync(idReport: String,idPv: String,state: String,type:String)

    @Query("SELECT count(id) FROM sku WHERE idPv =:idPv and idUser=:idUser  ")
    fun getCountSku(idPv: String,idUser: String):Int

    @Query("DELETE FROM reports_photo")
    fun deletePhotos()

}