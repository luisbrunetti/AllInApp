package com.tawa.allinapp.data.repository

import com.tawa.allinapp.core.functional.Either
import com.tawa.allinapp.core.functional.Failure
import com.tawa.allinapp.core.functional.NetworkHandler
import com.tawa.allinapp.data.local.Prefs
import com.tawa.allinapp.data.local.datasource.ParametersDataSource
import com.tawa.allinapp.data.local.datasource.ReportsDataSource
import com.tawa.allinapp.data.remote.MovieDetailEntity
import com.tawa.allinapp.data.remote.service.ParametersService
import com.tawa.allinapp.data.remote.service.QuestionsService
import com.tawa.allinapp.data.remote.service.ReportsService
import com.tawa.allinapp.models.Company
import com.tawa.allinapp.models.PV
import com.tawa.allinapp.models.PhotoReport
import com.tawa.allinapp.models.Report
import com.tawa.allinapp.models.ReportStatus
import retrofit2.Call
import javax.inject.Inject

interface ReportsRepository {
    fun setReports(company: String): Either<Failure, Boolean>
    fun saveLocalPhotoReport(report:PhotoReport): Either<Failure, Boolean>
    fun getReports(): Either<Failure,List<Report>>
    fun savePhotoReport(): Either<Failure, Boolean>
    fun getReportStatus(): Either<Failure, List<ReportStatus>>

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
                        val response = service.getReportStatus().execute()
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

        override fun getReports(): Either<Failure, List<Report>> {
            return try {
                Either.Right(reportsDataSource.getReports().map { it.toView() })
            }catch (e:Exception){
                Either.Left(Failure.DefaultError(e.message!!))
            }
        }
    }
}