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

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertPhotoReport(report:PhotoReportModel)

    @Query("SELECT * FROM reports_photo")
    fun getPhotoReports(): List<PhotoReportModel>

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

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertSkuObservation(skuObservationModel: SkuObservationModel)

    @Query("SELECT * FROM sku_observation where idSkuDetail = :idSkuDetail")
    fun getSkuObservation(idSkuDetail:String):List<SkuObservationModel>

    @Query("UPDATE sku_detail set stock=:stock , exhibition=:exhi,newPrice=:price WHERE id=:idSkuDetail")
    fun updateSkuDetail(idSkuDetail:String,stock:Boolean,exhi:Boolean,price:Float)

}