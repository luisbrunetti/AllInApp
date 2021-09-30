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
import com.tawa.allinapp.data.remote.service.NotifyService
import com.tawa.allinapp.data.remote.service.PdvService
import com.tawa.allinapp.data.remote.service.QuestionsService
import com.tawa.allinapp.data.remote.service.RoutesService
import com.tawa.allinapp.features.init.usecase.GetIdCompany
import com.tawa.allinapp.models.*
import javax.inject.Inject

interface NotifyRepository {
    fun updateCountNotify(): Either<Failure, Boolean>
    fun getCountNotify(): Either<Failure, Int>
    fun clearNotify(): Either<Failure, Boolean>
    fun getNotify():Either<Failure, List<Notify>>

    class Network
    @Inject constructor(
                        private val networkHandler: NetworkHandler,
                        private val prefs: Prefs,
                        private val service: NotifyService
    ): NotifyRepository{

        override fun updateCountNotify(): Either<Failure, Boolean> {
            return try {
                prefs.countNotify++
                Either.Right(true)
            }catch (e:Exception){
                Either.Left(Failure.DefaultError(e.message!!))
            }
        }

        override fun getCountNotify(): Either<Failure, Int> {
            return try {
                Either.Right(prefs.countNotify)
            }catch (e:Exception){
                Either.Left(Failure.DefaultError(e.message!!))
            }
        }

        override fun clearNotify(): Either<Failure, Boolean> {
            return try {
                prefs.countNotify = 0
                Either.Right(true)
            }catch (e:Exception){
                Either.Left(Failure.DefaultError(e.message!!))
            }
        }

        override fun getNotify(): Either<Failure, List<Notify>> {
            return when (networkHandler.isConnected) {
                true ->{
                    try {
                        val response = service.getNotify("Bearer ${prefs.token!!}").execute()
                        when (response.isSuccessful) {
                            true -> {
                                response.body()?.let { body ->
                                    if(body.success) {
                                        Either.Right(body.data.map { it.toView(prefs.name?:"")})
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

    }
}