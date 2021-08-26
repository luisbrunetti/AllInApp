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
import com.tawa.allinapp.models.Answer
import com.tawa.allinapp.models.Question
import com.tawa.allinapp.models.ReadyAnswer
import com.tawa.allinapp.models.RoutesUser
import javax.inject.Inject

interface RoutesRepository {
    fun getListUserRemote(): Either<Failure, List<RoutesUser>>


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

    }
}