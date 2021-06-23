package com.tawa.allinapp.data.repository

import com.tawa.allinapp.core.functional.Either
import com.tawa.allinapp.core.functional.Failure
import com.tawa.allinapp.data.local.Prefs
import com.tawa.allinapp.data.local.datasource.CheckDataSource
import com.tawa.allinapp.models.Check
import javax.inject.Inject

interface CheckRepository {

    fun setCheck(checkIn: Check): Either<Failure, Boolean>
    fun setIdCompany(idCompany:String): Either<Failure, Boolean>
    fun setIdPv(idPv:String,namePv:String): Either<Failure, Boolean>
    fun getDescPv(): Either<Failure, String>
    fun getIdPv(): Either<Failure, String>
    fun getIdCompany(): Either<Failure, String>
    fun getIdUser(): Either<Failure, String>
    fun getCheckMode(): Either<Failure, Boolean>
    fun getStateCheck(idPv: String): Either<Failure, Boolean>

    class Network
    @Inject constructor(private val checkDataSource: CheckDataSource,
                        private val prefs: Prefs,
    ): CheckRepository{

        override fun setCheck(checkIn: Check): Either<Failure, Boolean> {
            return try {
                checkDataSource.insertCheck(checkIn.toModel())
                prefs.checkIn = !prefs.checkIn
                Either.Right(true)
            }catch (e:Exception){
                Either.Left(Failure.DefaultError(e.message!!))
            }
        }

        override fun setIdCompany(idCompany: String): Either<Failure, Boolean> {
            return try {
                prefs.companyId = idCompany
                Either.Right(true)
            }catch (e:Exception){
                Either.Left(Failure.DefaultError(e.message!!))
            }
        }

        override fun setIdPv(idPv: String,namePv:String): Either<Failure, Boolean> {
            return try {
                prefs.pvId = idPv
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
                Either.Right(prefs.pvName?:"")
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

        override fun getStateCheck(IdPv:String): Either<Failure, Boolean> {
            return try {
                if(checkDataSource.getStateCheck(IdPv)==0) Either.Right(true)
                else
                    Either.Right(false)

            }catch (e:Exception){
                Either.Left(Failure.DefaultError(e.message!!))
            }
        }

    }
}


