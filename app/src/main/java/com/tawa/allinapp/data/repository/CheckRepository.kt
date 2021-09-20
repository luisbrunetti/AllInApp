package com.tawa.allinapp.data.repository

import android.util.Log
import com.tawa.allinapp.core.functional.Either
import com.tawa.allinapp.core.functional.Failure
import com.tawa.allinapp.core.functional.NetworkHandler
import com.tawa.allinapp.data.local.Prefs
import com.tawa.allinapp.data.local.datasource.CheckDataSource
import com.tawa.allinapp.data.remote.service.CheckService
import com.tawa.allinapp.data.remote.service.ParametersService
import com.tawa.allinapp.models.Check
import javax.inject.Inject

interface CheckRepository {

    fun setCheck(id: Int,pv:String,idUser:String,registerDate:String,latitude:String,longitude:String,comment:String): Either<Failure, Boolean>
    fun setIdCompany(idCompany:String,image:String): Either<Failure, Boolean>
    fun setIdPv(schedule:String,pv:String,namePv:String): Either<Failure, Boolean>
    fun getDescPv(): Either<Failure, String>
    fun getLogoCompany(): Either<Failure, String>
    fun getIdPv(): Either<Failure, String>
    fun getIdCompany(): Either<Failure, String>
    fun getIdUser(): Either<Failure, String>
    fun getRoleUser(): Either<Failure, String>
    fun getUserName(): Either<Failure, String>
    fun getCheckMode(): Either<Failure, Boolean>
    fun getStateCheck(idPv: String): Either<Failure, Boolean>
    fun syncChecks(): Either<Failure, Boolean>

    class Network
    @Inject constructor(private val networkHandler: NetworkHandler,
                        private val checkDataSource: CheckDataSource,
                        private val prefs: Prefs,
                        private val service: CheckService
    ): CheckRepository{

        override fun setCheck(id: Int,pv:String,idUser:String,registerDate:String,latitude:String,longitude:String,comment:String): Either<Failure, Boolean> {
            return try {
                val check = Check(id,prefs.schedule?:"",prefs.companyId?:"",pv,idUser,registerDate,latitude,longitude,comment)
                checkDataSource.insertCheck(check.toModel())
                prefs.checkIn = !prefs.checkIn
                Either.Right(true)
            }catch (e:Exception){
                Either.Left(Failure.DefaultError(e.message!!))
            }
        }

        override fun setIdCompany(idCompany: String,image:String): Either<Failure, Boolean> {
            return try {
                prefs.companyId = idCompany
                prefs.logoCompany  = image
                Either.Right(true)
            }catch (e:Exception){
                Either.Left(Failure.DefaultError(e.message!!))
            }
        }

        override fun syncChecks(): Either<Failure, Boolean>{
            return when (networkHandler.isConnected) {
                true ->{
                    try {
                        val request = checkDataSource.getChecks().map { it.toRemote() }
                        Log.d("requestSyncChecks ->> ",request.toString())
                        val response = service.syncChecks("Bearer ${prefs.token!!}", request).execute()
                        Log.d("responseSyncChecks ->> ",response.toString())
                        when (response.isSuccessful) {
                            true -> {
                                response.body()?.let { body ->
                                    if(body.success) {
                                        Log.d("syncChecks", "Se ha realizado la sincronización de Checks correctamente" )
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

        override fun setIdPv(schedule:String,pv:String,namePv:String): Either<Failure, Boolean> {
            return try {
                prefs.schedule = schedule
                prefs.pvId = pv
                prefs.pvName = namePv
                Either.Right(true)
            }catch (e:Exception){
                Either.Left(Failure.DefaultError(e.message!!))
            }
        }

        override fun getDescPv(): Either<Failure, String> {
            return try {
                Either.Right(prefs.pvName?:"")
            }catch (e:Exception){
                Either.Left(Failure.DefaultError(e.message!!))
            }
        }

        override fun getIdPv(): Either<Failure, String> {
            return try {
                Either.Right(prefs.pvId?:"")
            }catch (e:Exception){
                Either.Left(Failure.DefaultError(e.message!!))
            }
        }

        override fun getIdCompany(): Either<Failure, String> {
            return try {
                Either.Right(prefs.companyId!!)
            }catch (e:Exception){
                Either.Left(Failure.DefaultError(e.message!!))
            }
        }

        override fun getCheckMode(): Either<Failure, Boolean> {
            return try {
                Either.Right(prefs.checkIn)
            }catch (e:Exception){
                Either.Left(Failure.DefaultError(e.message!!))
            }
        }

        override fun getIdUser(): Either<Failure, String> {
            return try {
                Either.Right(prefs.idUser!!)
            }catch (e:Exception){
                Either.Left(Failure.DefaultError(e.message!!))
            }
        }

        override fun getUserName(): Either<Failure, String> {
            return try {
                Either.Right(prefs.name!!)
            }catch (e:Exception){
                Either.Left(Failure.DefaultError(e.message!!))
            }
        }

        override fun getStateCheck(idPv:String): Either<Failure, Boolean> {
            return try {
                if(checkDataSource.getStateCheck(idPv,prefs.idUser?:"")==0) Either.Right(true)
                else Either.Right(false)
            }catch (e:Exception){
                Either.Left(Failure.DefaultError(e.message!!))
            }
        }

        override fun getRoleUser(): Either<Failure, String> {
            return try {
                 Either.Right(prefs.role?:"")
            }catch (e:Exception){
                Either.Left(Failure.DefaultError(e.message!!))
            }
        }

        override fun getLogoCompany(): Either<Failure, String> {
            return try {
                Either.Right(prefs.logoCompany?:"")
            }catch (e:Exception){
                Either.Left(Failure.DefaultError(e.message!!))
            }
        }
    }
}


