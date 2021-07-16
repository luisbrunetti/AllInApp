package com.tawa.allinapp.data.repository

import com.tawa.allinapp.core.functional.Either
import com.tawa.allinapp.core.functional.Failure
import com.tawa.allinapp.core.functional.NetworkHandler
import com.tawa.allinapp.data.local.Prefs
import com.tawa.allinapp.data.remote.entities.LoginRemote
import com.tawa.allinapp.data.remote.service.AuthService
import retrofit2.Call
import javax.inject.Inject

interface AuthRepository {
    fun login(username: String, password: String): Either<Failure, Boolean>
    fun userLoggedIn(): Either<Failure, Boolean>

    class Network
    @Inject constructor(private val networkHandler: NetworkHandler,
                        private val prefs: Prefs,
                        private val service: AuthService
    ) : AuthRepository {

        override fun login(username: String, password: String): Either<Failure, Boolean> {
            return when (networkHandler.isConnected) {
                true ->{
                    try {
                        val response = service.login(LoginRemote.Request(username, password)).execute()
                        when (response.isSuccessful) {
                            true -> {
                                response.body()?.let { body ->
                                    if(body.success) {
                                        prefs.name = username
                                        prefs.user = body.data.user
                                        prefs.token = body.data.token
                                        prefs.idUser = body.data.idUser
                                        prefs.suoervisor = body.data.role.size > 1
                                        prefs.session = true
                                        prefs.checkIn = true
                                        Either.Right(true)
                                    }
                                    else
                                        Either.Left(Failure.DefaultError(body.message))
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

        override fun userLoggedIn(): Either<Failure, Boolean> {
            return Either.Right(prefs.session)
        }
    }
}
