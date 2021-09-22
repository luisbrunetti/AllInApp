package com.tawa.allinapp.data.repository

import android.util.Log
import com.tawa.allinapp.core.functional.Either
import com.tawa.allinapp.core.functional.Failure
import com.tawa.allinapp.core.functional.NetworkHandler
import com.tawa.allinapp.data.local.Prefs
import com.tawa.allinapp.data.remote.entities.GraphRemote
import com.tawa.allinapp.data.remote.service.DashboardService
import com.tawa.allinapp.models.*
import javax.inject.Inject

interface DashboardRepository {

    fun getChannels(): Either<Failure, List<Channel>>
    fun getRetails(): Either<Failure, List<Retail>>
    fun getChains(channel:List<String>,retail:List<String>): Either<Failure, List<Chain>>
    fun getUserList(): Either<Failure, List<User>>
    fun getGraph(dateStart:String?,dateEnd:String?,user:List<String>?,chains:List<String>?): Either<Failure, CoverageGraph>

    class Network
    @Inject constructor(private val networkHandler: NetworkHandler,
                        private val prefs: Prefs,
                        private val service: DashboardService
    ) : DashboardRepository {
        override fun getChannels(): Either<Failure, List<Channel>> {
            return when (networkHandler.isConnected) {
                true ->{
                    try {
                        val response = service.getChannels("Bearer ${prefs.token!!}", prefs.companyId!!).execute()
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
        override fun getRetails(): Either<Failure, List<Retail>> {
            return when (networkHandler.isConnected) {
                true ->{
                    try {
                        val response = service.getRetails("Bearer ${prefs.token!!}", prefs.companyId!!).execute()
                        when (response.isSuccessful) {
                            true -> {
                                response.body()?.let { body ->
                                    if(body.success) {
                                        Log.d("sucessRetail",
                                            body.data.map { it.toModel() }.toString()
                                        )
                                        Either.Right(body.data.map { it.toModel() })
                                    }
                                    else{
                                        Either.Left(Failure.DefaultError(body.message))
                                    }
                                }?: Either.Left(Failure.DefaultError(""))
                            }
                            false -> Either.Left(Failure.ServerError)
                        }
                    } catch (e: Exception) {
                        Log.d("getReatails", e.toString())
                        Either.Left(Failure.DefaultError(e.message!!))
                    }
                }
                false -> Either.Left(Failure.NetworkConnection)
            }
        }
        override fun getChains(channel:List<String>,retail:List<String>): Either<Failure, List<Chain>> {
            return when (networkHandler.isConnected) {
                true ->{
                    try {
                        Log.d("Params_getchain", "idCompanny -> ${prefs.companyId} channel -> $channel  retail --> $retail" )
                        val response = service.getChains("Bearer ${prefs.token!!}", prefs.companyId!!,channel,retail).execute()
                        when (response.isSuccessful) {
                            true -> {
                                response.body()?.let { body ->
                                    if(body.success) {
                                        Either.Right(body.data.map { it.toModel() })
                                    }
                                    else{
                                        Either.Left(Failure.DefaultError(body.message))
                                    }
                                }?: Either.Left(Failure.DefaultError(""))
                            }
                            false -> Either.Left(Failure.ServerError)
                        }
                    } catch (e: Exception) {
                        Log.d("getChains", e.toString())
                        Either.Left(Failure.DefaultError(e.message!!))
                    }
                }
                false -> Either.Left(Failure.NetworkConnection)
            }
        }
        override fun getUserList(): Either<Failure, List<User>> {
            return when (networkHandler.isConnected) {
                true ->{
                    try {
                        val response = service.getUserList("Bearer ${prefs.token!!}", prefs.companyId!!).execute()
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
        override fun getGraph(dateStart:String?,dateEnd:String?,user:List<String>?,chains:List<String>?): Either<Failure, CoverageGraph> {
            return when (networkHandler.isConnected) {
                true ->{
                    try {
                        Log.d("getGraph", "dateStart -> $dateStart \ndataEmd -> $dateEnd\nuser -> $user\nchains -> $chains")
                        val response = service.getGraph(
                            "Bearer ${prefs.token!!}",
                            prefs.companyId!!,
                            dateStart,
                            dateEnd,
                            user,
                            chains
                        ).execute()
                        when (response.isSuccessful) {
                            true -> {
                                response.body()?.let { body ->
                                    if(body.success) {
                                        Either.Right(body.data.toModel())
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