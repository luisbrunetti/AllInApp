package com.tawa.allinapp.data.repository

import com.tawa.allinapp.core.functional.Either
import com.tawa.allinapp.core.functional.Failure
import com.tawa.allinapp.data.local.Prefs
import com.tawa.allinapp.data.local.datasource.CheckDataSource
import com.tawa.allinapp.models.Check
import javax.inject.Inject

interface CheckRepository {

    fun setCheck(checkIn: Check): Either<Failure, Boolean>

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
    }
}


