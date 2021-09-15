package com.tawa.allinapp.data.repository


import android.util.Log
import com.tawa.allinapp.core.functional.Either
import com.tawa.allinapp.core.functional.Failure
import com.tawa.allinapp.core.functional.NetworkHandler
import com.tawa.allinapp.data.local.Prefs
import com.tawa.allinapp.data.local.datasource.QuestionsDataSource
import com.tawa.allinapp.data.local.datasource.ReportsDataSource
import com.tawa.allinapp.data.local.models.AnswersPvModel
import com.tawa.allinapp.data.local.models.ReadyAnswerModel
import com.tawa.allinapp.data.remote.entities.RoutesRemote
import com.tawa.allinapp.data.remote.service.QuestionsService
import com.tawa.allinapp.data.remote.service.RoutesService
import com.tawa.allinapp.models.*
import javax.inject.Inject

interface RoutesRepository {
    fun getListUserRemote(): Either<Failure, List<RoutesUser>>
    fun getRoutes(idUser:String,dateStart:String): Either<Failure, List<Routes>>
    fun getTracking(idUser:String,dateStart:String): Either<Failure, List<Tracking>>
    fun getRoutesFromListUsers(mutableListUser: MutableList<RoutesUser>, dateStart: String): Either<Failure,List<RoutesInform>>
    fun getTrackingFromListUsers(mutableListUser: MutableList<RoutesUser>, dateStart: String): Either<Failure,List<TrackingInform >>

    class Network
    @Inject constructor(private val networkHandler: NetworkHandler,
                        private val prefs: Prefs,
                        private val service: RoutesService,
    ): RoutesRepository{

        override fun getListUserRemote(): Either<Failure, List<RoutesUser>> {
            return when (networkHandler.isConnected) {
                true ->{
                    try {
                        val response = service.getListUser("Bearer ${prefs.token!!}").execute()
                        when (response.isSuccessful) {
                            true -> {
                                response.body()?.let { body ->
                                    if(body.success) {
                                        Either.Right(body.data.map { it.toView() })
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

        override fun getRoutes(idUser:String,dateStart:String): Either<Failure, List<Routes>> {
            return when (networkHandler.isConnected) {
                true ->{
                    try {
                        val response = service.getRoutes("Bearer ${prefs.token!!}",prefs.companyId?:"",idUser,dateStart).execute()
                        when (response.isSuccessful) {
                            true -> {
                                response.body()?.let { body ->
                                    if(body.success) {
                                        Either.Right(body.data.map { it.toView() })
                                    }
                                    else {
                                        Log.d("errorTracking",body.message.toString())
                                        Either.Left(Failure.DefaultError(body.message))
                                    }
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

        override fun getTracking(idUser: String, dateStart: String): Either<Failure, List<Tracking>> {
            return when (networkHandler.isConnected) {
                true ->{
                    try {
                        val response = service.getTracking("Bearer ${prefs.token!!}",prefs.companyId?:"",idUser,dateStart).execute()
                        when (response.isSuccessful) {
                            true -> {
                                response.body()?.let { body ->
                                    if(body.success) {
                                        Either.Right(body.data.map { it.toView() })
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

        override fun getRoutesFromListUsers(mutableListUser: MutableList<RoutesUser>, dateStart: String): Either<Failure, List<RoutesInform>> {
            val arrayResponse : ArrayList<RoutesInform> = ArrayList()
            try{
                for(user in mutableListUser){
                    Log.d("response",user.id + user.name)
                    //611e7bae4a9f0712991ba1e1
                    val response = getRoutes(user.id,"2021-08-21")
                    response.either({
                        Log.d("failure", it.toString())
                        arrayResponse.add(RoutesInform(user.id,user.name, emptyList()))
                    },{
                        Log.d("success", it.toString())
                        val routeInform = RoutesInform(user.id,user.name,it)
                        arrayResponse.add(routeInform)
                    })
                }
                Log.d("arrayResponse", arrayResponse.toString())
                return Either.Right(arrayResponse)
            }catch (e : Exception){
                return Either.Left(Failure.DefaultError(e.message!!))
            }
        }

        override fun getTrackingFromListUsers(
            mutableListUser: MutableList<RoutesUser>,
            dateStart: String
        ): Either<Failure, List<TrackingInform>> {
            val arrayResponse : ArrayList<TrackingInform> = ArrayList()
            try{
                for(user in mutableListUser){
                    //val response = getTracking(user.id,"2021-08-26")
                        Log.d("request", user.id + "  "+ dateStart.toString())
                    val response = getTracking(user.id,dateStart)
                    response.either({
                        arrayResponse.add(TrackingInform(user.id,user.name, emptyList()))
                    },{
                        val routeInform = TrackingInform(user.id,user.name,it)
                        arrayResponse.add(routeInform)
                    })
                }
                Log.d("arrayResponse", arrayResponse.toString())
                return Either.Right(arrayResponse)
            }catch (e : Exception){
                return Either.Left(Failure.DefaultError(e.message!!))
            }
        }
    }
}