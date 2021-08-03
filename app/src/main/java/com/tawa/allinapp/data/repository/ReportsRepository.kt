package com.tawa.allinapp.data.repository

import android.util.Log
import com.tawa.allinapp.core.dialog.MessageDialogFragment
import com.tawa.allinapp.core.functional.Either
import com.tawa.allinapp.core.functional.Failure
import com.tawa.allinapp.core.functional.NetworkHandler
import com.tawa.allinapp.data.local.Prefs
import com.tawa.allinapp.data.local.datasource.ReportsDataSource
import com.tawa.allinapp.data.local.models.PhotoReportModel
import com.tawa.allinapp.data.local.models.SkuDetailModel
import com.tawa.allinapp.data.local.models.SkuModel
import com.tawa.allinapp.data.remote.MovieDetailEntity
import com.tawa.allinapp.data.remote.entities.ReportsSkuRemote
import com.tawa.allinapp.data.remote.entities.UpdateStatusRemote
import com.tawa.allinapp.data.remote.service.ReportsService
import com.tawa.allinapp.models.AudioReport
import com.tawa.allinapp.models.PhotoReport
import com.tawa.allinapp.models.Report
import com.tawa.allinapp.models.ReportStatus
import com.tawa.allinapp.features.init.usecase.GetIdCompany
import com.tawa.allinapp.models.*
import retrofit2.Call
import javax.inject.Inject
import kotlin.math.log

interface ReportsRepository {
    fun setReports(company: String): Either<Failure, Boolean>
    fun saveLocalPhotoReport(report:PhotoReport): Either<Failure, Boolean>
    fun getReports(): Either<Failure,List<Report>>
    fun getSkuDetail(idSku:String): Either<Failure,List<SkuDetail>>
    fun getSku(): Either<Failure,List<Sku>>
    fun syncPhotoReports(): Either<Failure,Boolean>
    fun savePhotoReport(): Either<Failure, Boolean>
    fun getReportStatus(): Either<Failure, List<ReportStatus>>
    fun updateStatus(latitude:String,longitude:String,battery:String): Either<Failure, Boolean>
    fun saveLocalAudioReport(report: AudioReport): Either<Failure, Boolean>
    fun getReportsSku():Either<Failure, Boolean>
    fun insertSkuObservation(skuObservation: SkuObservation):Either<Failure, Boolean>
    fun getSkuObservation(idSkuDetail: String):Either<Failure, List<SkuObservation>>
    fun addSku(idReportPdv:String,idPv:String,idCompany: String,lines: List<Lines>):Either<Failure, Boolean>
    fun updateSkuDetail(idSkuDetail: String,stock:Boolean,exhibition:Boolean,price:Float):Either<Failure, Boolean>
    fun updateStateReport(idReport:String,state:String):Either<Failure, Boolean>
    fun getUserType():Either<Failure, String>

    class Network
    @Inject constructor(private val networkHandler: NetworkHandler,
                        private val reportsDataSource: ReportsDataSource,
                        private val prefs: Prefs,
                        private val service: ReportsService,
    ): ReportsRepository{

        override fun setReports(company:String): Either<Failure, Boolean> {
            return when (networkHandler.isConnected) {
                true ->{
                    try {
                        val response = service.getReports(company).execute()
                        when (response.isSuccessful) {
                            true -> {
                                response.body()?.let { body ->
                                    if(body.success) {
                                        body.data.map {
                                            reportsDataSource.insertReports(it.toModel())
                                        }
                                        Either.Right(true)
                                    }
                                    else Either.Left(Failure.DefaultError(body.message))
                                }?: Either.Left(Failure.DefaultError(""))
                            }
                            false -> Either.Left(Failure.ServerError)
                        }
                    } catch (e: Exception) {
                        Either.Left(Failure.DefaultError(e.message!!))
                    }
                }
                false -> Either.Left(Failure.NetworkConnection)
            }
        }

        override fun syncPhotoReports(): Either<Failure,Boolean>{
            return when (networkHandler.isConnected) {
                true ->{
                    try {
                        val request = reportsDataSource.getPhotoReports().map { it.toRemote() }
                        val response = service.syncPhotoReports("Bearer ${prefs.token!!}", request).execute()
                        when (response.isSuccessful) {
                            true -> {
                                response.body()?.let { body ->
                                    if(body.success) {
                                        Either.Right(true)
                                    }
                                    else Either.Left(Failure.DefaultError(body.message))
                                }?: Either.Left(Failure.DefaultError(""))
                            }
                            false -> Either.Left(Failure.ServerError)
                        }
                    } catch (e: Exception) {
                        Either.Left(Failure.DefaultError(e.message!!))
                    }
                }
                false -> Either.Left(Failure.NetworkConnection)
            }
        }

        override fun saveLocalPhotoReport(report:PhotoReport): Either<Failure, Boolean> {
            if (prefs.pvId!!.isEmpty())
                return Either.Left(Failure.DefaultError("Debe seleccionar hacer Checkin en un Punto de Venta"))
            else
                return try {
                    reportsDataSource.insertPhotoReport(
                        PhotoReportModel(
                            0,
                            prefs.companyId,
                            prefs.pvId,
                            report.before[0],
                            report.before[1],
                            report.before[2],
                            report.before[3],
                            report.before[4],
                            report.after[0],
                            report.after[1],
                            report.after[2],
                            report.after[3],
                            report.after[4],
                            report.comments,
                            report.createAt
                        )
                    )
                    Either.Right(true)
                }catch (e:Exception){
                    Either.Left(Failure.DefaultError(e.message!!))
                }
        }

        override fun savePhotoReport(): Either<Failure, Boolean> {
            return when (networkHandler.isConnected) {
                true ->{
                    try {
                        val reports = reportsDataSource.getPhotoReports()
                        var regs = 0
                        reports.map {
                            val response = service.setPhotoReports(
                                it.before1?:"",it.before2?:"",it.before3?:"",it.before4?:"",it.before5?:"",
                                it.after1?:"",it.after2?:"",it.after3?:"",it.after4?:"",it.after5?:"",it.comments?:""
                            ).execute()
                            if (response.isSuccessful) regs++
                        }
                        if (regs == reports.size) Either.Right(true) else Either.Left(Failure.DefaultError("No se insertaron algunos registros"))
                    } catch (e: Exception) {
                        Either.Left(Failure.DefaultError(e.message!!))
                    }
                }
                false -> Either.Left(Failure.NetworkConnection)
            }
        }

        override fun getReportStatus(): Either<Failure, List<ReportStatus>> {
            return when (networkHandler.isConnected) {
                true ->{
                    try {
                        val response = service.getReportStatus("Bearer ${prefs.token!!}").execute()
                        when (response.isSuccessful) {
                            true -> {
                                response.body()?.let { body ->
                                    if(body.success) {
                                        Either.Right(body.data.map {
                                            it.toView()
                                        })
                                    }
                                    else Either.Left(Failure.DefaultError(body.message))
                                }?: Either.Left(Failure.DefaultError(""))
                            }
                            false -> Either.Left(Failure.ServerError)
                        }
                    } catch (e: Exception) {
                        Either.Left(Failure.DefaultError(e.message!!))
                    }
                }
                false -> Either.Left(Failure.NetworkConnection)
            }

        }

        override fun updateStatus(latitude: String,longitude: String,battery: String): Either<Failure, Boolean> {
            return when (networkHandler.isConnected) {
                true ->{
                    try {
                        val response = service.updateStatus("Bearer ${prefs.token!!}",UpdateStatusRemote.Request(latitude,longitude,battery)).execute()
                        when (response.isSuccessful) {
                            true -> {
                                response.body()?.let { body ->
                                    if(body.success) {
                                        Either.Right(true)
                                    }
                                    else Either.Left(Failure.DefaultError(body.message))
                                }?: Either.Left(Failure.DefaultError(""))
                            }
                            false -> Either.Left(Failure.ServerError)
                        }
                    } catch (e: Exception) {
                        Either.Left(Failure.DefaultError(e.message!!))
                    }
                }
                false -> Either.Left(Failure.NetworkConnection)
            }
        }

        override fun getReportsSku(): Either<Failure, Boolean> {
            return when (networkHandler.isConnected) {
                true ->{
                    try {
                        val response = service.getReportsSku("Bearer ${prefs.token!!}").execute()

                        when (response.isSuccessful) {
                            true -> {
                                response.body()?.let { body ->
                                    body.data.map {
                                        reportsDataSource.insertSku(SkuModel(it.id,it.idPuntoVenta.id,it.idEmpresa.id))
                                        for(products in it.lineas)
                                        {
                                            products.idProducto.nombreProducto?.let { it1 ->
                                                products.idProducto.idSubsegmentoProd?.idSegmentoProd?.idSubcategoriaProd?.nombreSubcategoria?.let { it2 ->
                                                    products.idProducto.idSubsegmentoProd?.idSegmentoProd?.idSubcategoriaProd?.idCategoriaProd?.nombreCategoria?.let { it3 ->
                                                        SkuDetailModel(products.id,products.idProducto.feCreacion,products.idProducto.id,
                                                            it1,
                                                            it3,
                                                            it2,products.inventario,products.precio,false,false,0.0f,it.id)
                                                    }
                                                }
                                            }?.let { it2 -> reportsDataSource.insertSkuDetail(it2) }
                                        }
                                    }
                                    Either.Right(true)
                                }?: Either.Left(Failure.DefaultError(""))
                            }
                            false -> Either.Left(Failure.ServerError)
                        }
                    } catch (e: Exception) {
                        Either.Left(Failure.DefaultError(e.message!!))
                    }
                }
                false -> Either.Left(Failure.NetworkConnection)
            }
        }

        override fun getReports(): Either<Failure, List<Report>> {
            return try {
                Either.Right(reportsDataSource.getReports().map { it.toView() })
            }catch (e:Exception){
                Either.Left(Failure.DefaultError(e.message!!))
            }
        }

        override fun saveLocalAudioReport(report: AudioReport): Either<Failure, Boolean> {
            return try {
                reportsDataSource.insertAudioReport(report.toModel())
                Either.Right(true)
            }catch (e:Exception){
                Either.Left(Failure.DefaultError(e.message!!))
            }
        }

        override fun getSkuDetail(idSku: String): Either<Failure, List<SkuDetail>> {
            return try {
                Either.Right(reportsDataSource.getSkuDetail(idSku).map { it.toView() })
            }catch (e:Exception){
                Either.Left(Failure.DefaultError(e.message!!))
            }
        }

        override fun insertSkuObservation(skuObservation: SkuObservation): Either<Failure, Boolean> {
            return try {
                reportsDataSource.insertSkuObservation(skuObservation.toModel())
                Either.Right(true)
            }catch (e:Exception){
                Either.Left(Failure.DefaultError(e.message!!))
            }
        }

        override fun getSkuObservation(idSkuDetail: String): Either<Failure, List<SkuObservation>> {
            return try {
                Either.Right(reportsDataSource.getSkuObservation(idSkuDetail).map { it.toView() })
            }catch (e:Exception){
                Either.Left(Failure.DefaultError(e.message!!))
            }
        }

        override fun getSku(): Either<Failure, List<Sku>> {
            return try {
                Either.Right(reportsDataSource.getSku("60fb181d8b978fb259e4acb8").map { it.toView() })
            }catch (e:Exception){
                Either.Left(Failure.DefaultError(e.message!!))
            }
        }

        override fun addSku(
            idReportPdv: String,
            idPv: String,
            idCompany: String,
            lines: List<Lines>
        ): Either<Failure, Boolean> {
            return when (networkHandler.isConnected) {
                true ->{
                    try {
                        val response = service.addSku("Bearer ${prefs.token!!}",ReportsSkuRemote.Request(idReportPdv,idPv,idCompany,lines.map { it.toRequest() })).execute()
                        when (response.isSuccessful) {
                            true -> {
                                response.body()?.let { body ->
                                    if(body.success) {
                                        Either.Right(true)
                                    }
                                    else Either.Left(Failure.DefaultError(body.message))
                                }?: Either.Left(Failure.DefaultError(""))
                            }
                            false -> Either.Left(Failure.ServerError)
                        }
                    } catch (e: Exception) {
                        Either.Left(Failure.DefaultError(e.message!!))
                    }
                }
                false -> Either.Left(Failure.NetworkConnection)
            }
        }

        override fun updateSkuDetail(
            idSkuDetail: String,
            stock: Boolean,
            exhibition: Boolean,
            price: Float
        ): Either<Failure, Boolean> {
            return try {
                reportsDataSource.updateSkuDetail(idSkuDetail,stock,exhibition,price)
                Either.Right(true)
            }catch (e:Exception){
                Either.Left(Failure.DefaultError(e.message!!))
            }
        }

        override fun updateStateReport(idReport: String, state: String): Either<Failure, Boolean> {
            return try {
                reportsDataSource.updateStateReports("60dc7d0c11bb190a40e28e87",state)
                Either.Right(true)
            }catch (e:Exception){
                Either.Left(Failure.DefaultError(e.message!!))
            }
        }

        override fun getUserType(): Either<Failure, String> {
            return try {
                Either.Right(prefs.role?:"")
            }catch (e:Exception){
                Either.Left(Failure.DefaultError(e.message!!))
            }
        }

    }
}