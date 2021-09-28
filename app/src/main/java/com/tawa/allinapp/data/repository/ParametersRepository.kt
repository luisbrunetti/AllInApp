package com.tawa.allinapp.data.repository

import android.util.Log
import com.tawa.allinapp.core.functional.Either
import com.tawa.allinapp.core.functional.Failure
import com.tawa.allinapp.core.functional.NetworkHandler
import com.tawa.allinapp.data.local.Prefs
import com.tawa.allinapp.data.local.datasource.ParametersDataSource
import com.tawa.allinapp.data.remote.service.ParametersService
import com.tawa.allinapp.models.Company
import com.tawa.allinapp.models.Schedule
import javax.inject.Inject

interface ParametersRepository {
    fun setCompanies(): Either<Failure, Boolean>
    fun getCompanies(): Either<Failure,List<Company>>
    fun setPV(idCompany: String): Either<Failure, Boolean>
    fun getPV(company:String): Either<Failure, List<Schedule>>

    class Network
    @Inject constructor(private val networkHandler: NetworkHandler,
                        private val parametersDataSource: ParametersDataSource,
                        private val prefs: Prefs,
                        private val service: ParametersService
    ): ParametersRepository{
        override fun setCompanies(): Either<Failure, Boolean> {
            return when (networkHandler.isConnected) {
                true ->{
                    try {
                        val response = service.getCompanies("Bearer ${prefs.token!!}").execute()
                        when (response.isSuccessful) {
                            true -> {
                                response.body()?.let { body ->
                                    if(body.success) {
                                        body.data.map {
                                            Log.d("Companies", it.id.toString())
                                            parametersDataSource.insertCompanies(it.toModel(prefs.idUser?:""))
                                            // Insertar los puntos de venta
                                            setPV(it.id)
                                        }
                                        prefs.companyId = ""
                                        //prefs.companyId = body.data[0].id
                                        Either.Right(true)
                                    }
                                    else Either.Left(Failure.DefaultError(body.message))
                                }?: Either.Left(Failure.DefaultError("Unauthorized"))
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

        override fun getCompanies(): Either<Failure, List<Company>> {
            return try {
                Either.Right(parametersDataSource.getCompanies(prefs.idUser?:"").map { it.toView() })
            }catch (e:Exception){
                Either.Left(Failure.DefaultError(e.message!!))
            }
        }

        override fun setPV(idCompany: String): Either<Failure, Boolean> {
            return when (networkHandler.isConnected) {
                true ->{
                    try {
                        val response = service.getSchedule("Bearer ${prefs.token!!}", idCompany).execute()
                        when (response.isSuccessful) {
                            true -> {
                                response.body()?.let { body ->
                                    if(body.success) {
                                        Log.d("setPV", body.data.toString())
                                        body.data.map {
                                            it.pv?.map { pv ->
                                                parametersDataSource.insertPV(pv.toModel(it.id?:"",it.userAssigned,pv.corp!!))
                                            }
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

        override fun getPV(company:String): Either<Failure, List<Schedule>> {
            Log.d("getPv",company.toString())
            Log.d("idUser",prefs.idUser.toString())
            Log.d("val", parametersDataSource.getPV(company,prefs.idUser?:"").isNotEmpty().toString())
            return try {
                if (parametersDataSource.getPV(company,prefs.idUser?:"").isNotEmpty())
                    Either.Right(parametersDataSource.getPV(company,prefs.idUser?:"").map { it.toView() })
                else
                    Either.Right(emptyList())
            }catch (e:Exception){
                Either.Left(Failure.DefaultError(e.message!!))
            }
        }


    }
}