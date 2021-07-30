package com.tawa.allinapp.data.repository

import android.util.Log
import com.tawa.allinapp.core.functional.Either
import com.tawa.allinapp.core.functional.Failure
import com.tawa.allinapp.core.functional.NetworkHandler
import com.tawa.allinapp.data.local.Prefs
import com.tawa.allinapp.data.local.datasource.ReportsDataSource
import com.tawa.allinapp.data.remote.entities.UpdateStatusRemote
import com.tawa.allinapp.data.remote.service.ReportsService
import com.tawa.allinapp.models.AudioReport
import com.tawa.allinapp.models.PhotoReport
import com.tawa.allinapp.models.Report
import com.tawa.allinapp.models.ReportStatus
import javax.inject.Inject

interface ReportsRepository {
    fun setReports(company: String): Either<Failure, Boolean>
    fun saveLocalPhotoReport(report:PhotoReport): Either<Failure, Boolean>
    fun getReports(): Either<Failure,List<Report>>
    fun syncPhotoReports(): Either<Failure,Boolean>
    fun getSku(): Either<Failure,String>
    fun savePhotoReport(): Either<Failure, Boolean>
    fun getReportStatus(): Either<Failure, List<ReportStatus>>
    fun updateStatus(latitude:String,longitude:String,battery:String): Either<Failure, Boolean>
    fun saveLocalAudioReport(report: AudioReport): Either<Failure, Boolean>
    fun getReportsSku():Either<Failure, Boolean>

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
            return try {
                reportsDataSource.insertPhotoReport(report.toModel())
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
                                    prefs.dataSku = body.data.toString()
                                    Log.d("dataaaaa",body.data.toString())
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

        override fun getSku(): Either<Failure, String> {
            return try {
                Either.Right(prefs.dataSku?:"")
            }catch (e:Exception){
                Either.Left(Failure.DefaultError(e.message!!))
            }
        }
    }
}