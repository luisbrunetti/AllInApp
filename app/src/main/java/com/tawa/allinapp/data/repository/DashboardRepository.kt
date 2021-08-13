package com.tawa.allinapp.data.repository

import com.tawa.allinapp.core.functional.Either
import com.tawa.allinapp.core.functional.Failure
import com.tawa.allinapp.core.functional.NetworkHandler
import com.tawa.allinapp.data.local.Prefs
import com.tawa.allinapp.data.remote.service.DashboardService
import com.tawa.allinapp.models.Channel
import javax.inject.Inject

interface DashboardRepository {

    fun getChannels(company:String): Either<Failure, List<Channel>>

    class Network
    @Inject constructor(private val networkHandler: NetworkHandler,
                        private val prefs: Prefs,
                        private val service: DashboardService
    ) : DashboardRepository {
        override fun getChannels(company:String): Either<Failure, List<Channel>> {
            return when (networkHandler.isConnected) {
                true ->{
                    try {
                        val response = service.getChannels("Bearer ${prefs.token!!}", company).execute()
                        when (response.isSuccessful) {
                            true -> {
                                response.body()?.let { body ->
                                    if(body.success) {
                                        Either.Right(body.data.map { it.toModel() })
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