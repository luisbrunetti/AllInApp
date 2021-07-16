package com.tawa.allinapp.data.repository

import com.tawa.allinapp.core.functional.Either
import com.tawa.allinapp.core.functional.Failure
import com.tawa.allinapp.core.functional.NetworkHandler
import com.tawa.allinapp.data.local.Prefs
import com.tawa.allinapp.data.local.datasource.ParametersDataSource
import com.tawa.allinapp.data.remote.service.ParametersService
import com.tawa.allinapp.models.Company
import com.tawa.allinapp.models.PV
import javax.inject.Inject

interface ParametersRepository {
    fun setCompanies(): Either<Failure, Boolean>
    fun getCompanies(): Either<Failure,List<Company>>
    fun setPV(): Either<Failure, Boolean>
    fun getPV(company:String): Either<Failure, List<PV>>

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
                        val response = service.getCompanies(prefs.idUser!!, "Bearer ${prefs.token!!}").execute()
                        when (response.isSuccessful) {
                            true -> {
                                response.body()?.let { body ->
                                    if(body.success) {
                                        body.data.map {
                                            parametersDataSource.insertCompanies(it.toModel())
                                        }
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
                Either.Right(parametersDataSource.getCompanies().map { it.toView() })
            }catch (e:Exception){
                Either.Left(Failure.DefaultError(e.message!!))
            }
        }

        override fun setPV(): Either<Failure, Boolean> {
            return when (networkHandler.isConnected) {
                true ->{
                    try {
                        val response = service.getPV().execute()
                        when (response.isSuccessful) {
                            true -> {
                                response.body()?.let { body ->
                                    if(body.success) {
                                        body.data.map {
                                            parametersDataSource.insertPV(it.toModel())
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

        override fun getPV(company:String): Either<Failure, List<PV>> {
            return try {
                Either.Right(parametersDataSource.getPV(company).map { it.toView() })
            }catch (e:Exception){
                Either.Left(Failure.DefaultError(e.message!!))
            }
        }


    }
}