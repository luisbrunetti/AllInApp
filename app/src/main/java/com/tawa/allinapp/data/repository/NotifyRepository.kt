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

    class Network
    @Inject constructor(
                        private val prefs: Prefs,
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

    }
}