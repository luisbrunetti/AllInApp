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
    fun setIdPv(idPv:String): Either<Failure, Boolean>
    fun getIdCompany(): Either<Failure, String>
    fun getIdUser(): Either<Failure, String>

    class Network
    @Inject constructor(private val checkDataSource: CheckDataSource,
                        private val prefs: Prefs,
    ): CheckRepository{

        override fun setCheck(checkIn: Check): Either<Failure, Boolean> {
            return try {
                checkDataSource.insertCheck(checkIn.toModel())
                prefs.checkIn = false
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

        override fun setIdPv(idPv: String): Either<Failure, Boolean> {
            return try {
                prefs.pvId = idPv
                Either.Right(true)
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

        override fun getIdUser(): Either<Failure, String> {
            return try {
                Either.Right(prefs.idUser!!)
            }catch (e:Exception){
                Either.Left(Failure.DefaultError(e.message!!))
            }
        }
    }
}


