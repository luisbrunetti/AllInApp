package com.tawa.allinapp.data.repository


import android.util.Log
import com.tawa.allinapp.core.functional.Either
import com.tawa.allinapp.core.functional.Failure
import com.tawa.allinapp.core.functional.NetworkHandler
import com.tawa.allinapp.data.local.Prefs
import com.tawa.allinapp.data.local.datasource.PdvDataSource
import com.tawa.allinapp.data.local.datasource.QuestionsDataSource
import com.tawa.allinapp.data.local.datasource.ReportsDataSource
import com.tawa.allinapp.data.local.models.AnswersPvModel
import com.tawa.allinapp.data.local.models.PdvModel
import com.tawa.allinapp.data.local.models.ReadyAnswerModel
import com.tawa.allinapp.data.remote.entities.PdvRemote
import com.tawa.allinapp.data.remote.entities.RoutesRemote
import com.tawa.allinapp.data.remote.service.PdvService
import com.tawa.allinapp.data.remote.service.QuestionsService
import com.tawa.allinapp.data.remote.service.RoutesService
import com.tawa.allinapp.features.init.usecase.GetIdCompany
import com.tawa.allinapp.models.*
import javax.inject.Inject

interface PdvRepository {
    fun getPdvRemote(idCompany: String): Either<Failure, Boolean>
    fun getPdv():Either<Failure, Pdv>
    fun updatePdvRemote(idPdv: String,nameUser:String,phoneUser:String,ruc:String,latitude:String,longitude:String,image:String): Either<Failure, Boolean>
    fun updatePdv(idPdv: String,nameUser:String,phoneUser:String,ruc:String,latitude:String,longitude:String,image:String,state:String): Either<Failure, Boolean>
    fun updatePdv(idPdv: String,state:String): Either<Failure, Boolean>

    class Network
    @Inject constructor(private val networkHandler: NetworkHandler,
                        private val prefs: Prefs,
                        private val service: PdvService,
                        private val pdvDataSource: PdvDataSource,
    ): PdvRepository{

        override fun getPdvRemote(idCompany: String): Either<Failure, Boolean> {
            return when (networkHandler.isConnected) {
                true ->{
                    try {
                        val response = service.getPdvCompany("Bearer ${prefs.token!!}",idCompany).execute()
                        when (response.isSuccessful) {
                            true -> {
                                response.body()?.let { body ->
                                    if(body.success) {
                                        body.data.map {
                                            pdvDataSource.insertPdv(it.toModel())
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

        override fun getPdv(): Either<Failure, Pdv> {
            return try {
                Either.Right(pdvDataSource.getPdv(prefs.pvId?:"").toView())
            }catch (e:Exception){
                Either.Left(Failure.DefaultError(e.message!!))
            }
        }

        override fun updatePdvRemote(
            idPdv: String,
            nameUser: String,
            phoneUser: String,
            ruc: String,
            latitude: String,
            longitude: String,
            image: String
        ): Either<Failure, Boolean> {
            return when (networkHandler.isConnected) {
                true ->{
                    try {
                        val response = service.updatePdvRemote("Bearer ${prefs.token!!}",idPdv,PdvRemote.Request(
                            PdvRemote.Fields(nameUser,phoneUser,ruc,latitude,longitude,image))).execute()
                        when (response.isSuccessful) {
                            true -> {
                                response.body()?.let { body ->
                                    if(body.success) {
                                        Log.d("success",body.message.toString())
                                        Either.Right(true)
                                    }
                                    else{
                                        Log.d("error",body.message.toString())
                                        Either.Left(Failure.DefaultError(body.message))
                                    }
                                }?: Either.Left(Failure.DefaultError(""))
                            }
                            false -> {
                                Log.d("eeer",response.message())
                                Either.Left(Failure.ServerError)}
                        }
                    } catch (e: Exception) {
                        Log.d("error",e.message.toString())
                        Either.Left(Failure.DefaultError(e.message!!))
                    }
                }
                false -> Either.Left(Failure.NetworkConnection)
            }
        }

        override fun updatePdv(
            idPdv: String,
            nameUser: String,
            phoneUser: String,
            ruc: String,
            latitude: String,
            longitude: String,
            image: String,
            state: String
        ): Either<Failure, Boolean> {
            return try {
                pdvDataSource.updatePdv(idPdv,nameUser,phoneUser,ruc,latitude,longitude,image,state)
                Either.Right(true)
            }catch (e:Exception){
                Either.Left(Failure.DefaultError(e.message!!))
            }
        }

        override fun updatePdv(idPdv: String, state: String): Either<Failure, Boolean> {
            return try {
                pdvDataSource.updatePdv(idPdv,state)
                Either.Right(true)
            }catch (e:Exception){
                Either.Left(Failure.DefaultError(e.message!!))
            }
        }

    }
}