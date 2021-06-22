package com.tawa.allinapp.data.repository

import com.tawa.allinapp.core.functional.Either
import com.tawa.allinapp.core.functional.Failure
import com.tawa.allinapp.data.local.Prefs
import com.tawa.allinapp.data.local.datasource.CheckDataSource
import com.tawa.allinapp.data.local.datasource.ParametersDataSource
import com.tawa.allinapp.models.CheckIn
import javax.inject.Inject

interface CheckRepository {

    fun setCheckIn(checkIn: CheckIn): Either<Failure, Boolean>

    class Network
    @Inject constructor(private val parametersDataSource: CheckDataSource,
                        private val prefs: Prefs,
    ): CheckRepository{

        override fun setCheckIn(checkIn: CheckIn): Either<Failure, Boolean> {
            return try {
                parametersDataSource.insertCheckIn(checkIn.toModel())
                prefs.checkIn = false
                Either.Right(true)
            }catch (e:Exception){
                Either.Left(Failure.DefaultError(e.message!!))
            }
        }
    }
}


