package com.tawa.allinapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tawa.allinapp.data.local.models.*
import com.tawa.allinapp.models.Sku

@Dao
interface ReportsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertReports(reportModel: ReportModel)

    @Query("SELECT * FROM reports")
    fun getReports(): List<ReportModel>

    @Query("UPDATE reports set state=:state WHERE id=:idReport")
    fun updateStateReports(idReport:String,state:String)

    @Query("UPDATE sku set state=:state WHERE id=:idSku")
    fun updateStateSku(idSku:String,state:String)

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

    @Query("SELECT * FROM sku WHERE idPv=:idPv")
    fun getSku(idPv:String): List<SkuModel>

    @Query("SELECT state FROM sku WHERE idPv=:idPv")
    fun getStateSku(idPv:String): String

    @Query("SELECT state FROM reports_photo WHERE pv=:pv")
    fun getStatePhoto(pv:String): String

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertSkuObservation(skuObservationModel: SkuObservationModel)

    @Query("SELECT * FROM sku_observation where idSkuDetail = :idSkuDetail")
    fun getSkuObservation(idSkuDetail:String):List<SkuObservationModel>

    @Query("UPDATE sku_detail set stock=:stock , exhibition=:exhi,newPrice=:price WHERE id=:idSkuDetail")
    fun updateSkuDetail(idSkuDetail:String,stock:Boolean,exhi:Boolean,price:Float)

}