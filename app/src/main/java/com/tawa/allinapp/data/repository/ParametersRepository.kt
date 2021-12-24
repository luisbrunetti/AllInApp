package com.tawa.allinapp.data.repository


import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tawa.allinapp.BuildConfig
import com.tawa.allinapp.core.functional.Either
import com.tawa.allinapp.core.functional.Failure
import com.tawa.allinapp.core.functional.NetworkHandler
import com.tawa.allinapp.core.platform.TranslateObject
import com.tawa.allinapp.data.local.Prefs
import com.tawa.allinapp.data.local.datasource.ParametersDataSource
import com.tawa.allinapp.data.local.datasource.QuestionsDataSource
import com.tawa.allinapp.data.local.datasource.ReportsDataSource
import com.tawa.allinapp.data.local.models.AnswerModel
import com.tawa.allinapp.data.local.models.ReportModel
import com.tawa.allinapp.data.remote.api.ParametersApi
import com.tawa.allinapp.data.remote.service.ParametersService
import com.tawa.allinapp.data.remote.service.QuestionsService
import com.tawa.allinapp.data.remote.service.ReportsService
import com.tawa.allinapp.models.Company
import com.tawa.allinapp.models.Schedule
import com.tawa.allinapp.models.Translate
import com.tawa.allinapp.models.TranslateItem
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

interface ParametersRepository {
    fun setCompanies(): Either<Failure, Boolean>
    fun setQuestions(idReport: String): Either<Failure, Boolean>
    fun setReports(company: String): Either<Failure, Boolean>
    fun setCompaniesSync(): Either<Failure, Boolean>
    fun getCompanies(): Either<Failure,List<Company>>
    fun setPV(idCompany: String): Either<Failure, Boolean>
    fun getPV(company:String): Either<Failure, List<Schedule>>
    fun setLanguage(language:Int) : Either<Failure, Boolean>
    fun getLanguage(): Either<Failure,List<TranslateItem>>
    fun getTranslate(): Either<Failure,List<TranslateItem>>

    class Network
    @Inject constructor(private val networkHandler: NetworkHandler,
                        private val parametersDataSource: ParametersDataSource,
                        private val reportsDataSource: ReportsDataSource,
                        private val questionsDataSource: QuestionsDataSource,
                        private val prefs: Prefs,
                        private val service: ParametersService,
                        private val serviceReports: ReportsService,
                        private val serviceQuestions: QuestionsService,
                        private val translateObject: TranslateObject
    ): ParametersRepository{
        override fun setCompanies(): Either<Failure, Boolean> {
            return when (networkHandler.isConnected) {
                true ->{
                    try {
                        val response = service.getCompanies("Bearer ${prefs.token!!}").execute()
                        when (response.isSuccessful) {
                            true -> {
                                response.body()?.let { body ->
                                    Log.d("responseCompany",response.body()!!.data.toString())
                                    if(body.success) {
                                        body.data.map {
                                            Log.d("setCompanies",it.toModel(prefs.idUser?:"").toString())
                                            parametersDataSource.insertCompanies(it.toModel(prefs.idUser?:""))
                                            setPV(it.id)
                                        }
                                        prefs.companyId = ""
                                        Either.Right(true)
                                    }
                                    else Either.Left(Failure.DefaultError(body.message))
                                }?: Either.Left(Failure.DefaultError("Unauthorized"))
                            }
                            false -> Either.Left(Failure.ServerError)
                        }
                    } catch (e: Exception) {
                        Either.Left(Failure.DefaultError(e.message))
                    }
                }
                false -> Either.Left(Failure.NetworkConnection)
            }
        }

        override fun setCompaniesSync(): Either<Failure, Boolean> {
            return when (networkHandler.isConnected) {
                true ->{
                    try {
                        val response = service.getCompanies("Bearer ${prefs.token!!}").execute()
                        when (response.isSuccessful) {
                            true -> {
                                response.body()?.let { body ->
                                    Log.d("responseCompany",response.body()!!.data.toString())
                                    if(body.success) {
                                        body.data.map {
                                            Log.d("setCompanies",it.toModel(prefs.idUser?:"").toString())
                                            parametersDataSource.insertCompanies(it.toModel(prefs.idUser?:""))
                                            setPV(it.id)
                                            setReports(it.id)
                                        }
                                        Either.Right(true)
                                    }
                                    else Either.Left(Failure.DefaultError(body.message))
                                }?: Either.Left(Failure.DefaultError("Unauthorized"))
                            }
                            false -> Either.Left(Failure.ServerError)
                        }
                    } catch (e: Exception) {
                        Either.Left(Failure.DefaultError(e.message))
                    }
                }
                false -> Either.Left(Failure.NetworkConnection)
            }
        }

        override fun setReports(company:String): Either<Failure, Boolean> {
            return when (networkHandler.isConnected) {
                true ->{
                    try {
                        //Log.d("setReports", "token -> ${prefs.token!!} company -> ${company}")
                        val response = serviceReports.getReports("Bearer ${prefs.token!!}",company).execute()
                        when (response.isSuccessful) {
                            true -> {
                                response.body()?.let { body ->
                                    if(body.success) {
                                        body.data.map {

                                            for(type in it.reports)
                                            {
                                                //Log.d("type",type.reportName.toString())
                                                reportsDataSource.insertReports(ReportModel(type.id?:"",type.reportName?:"",it.idCompany.id?:"",it.idCompany.nameCompany?:"",it.userAsig.id?:"",it.idUserMod?:"",it.feMod?:"","","No iniciado","0",""))
                                                setQuestions(type.id?:"")
                                            }
                                            // reportsDataSource.insertReports(it.toModel())
                                            Log.d("reportes",it.toString())
                                        }
                                        Either.Right(true)
                                    }
                                    else Either.Left(Failure.DefaultError(body.message))
                                }?: Either.Left(Failure.ServerError)
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

        override fun setQuestions(idReport: String): Either<Failure, Boolean> {
            return when (networkHandler.isConnected) {
                true ->{
                    try {
                        val response = serviceQuestions.getQuestions("Bearer ${prefs.token!!}",idReport).execute()
                        when (response.isSuccessful) {
                            true -> {
                                response.body()?.let { body ->
                                    if(body.success) {
                                        body.data.map {
                                            Log.d("PREGUNTAS",it.toString())
                                            questionsDataSource.insertQuestions(it.question.toModel())
                                            for(answers in it.answers )
                                                questionsDataSource.insertAnswers(AnswerModel(answers.id,answers.status,answers.answerName,answers.order,answers.idQuestion,answers.idUserCreator,answers.idUserModifier,answers.dateModify,answers.dateCreation,"",it.question.questionName,it.question.objectType,answers.row,answers.column,it.question.order.toInt().toString(),it.question.required,""))
                                            it.subAnswers.map { sub ->
                                                if (sub != null) {
                                                    questionsDataSource.insertAnswers(AnswerModel(sub.id,"",sub.subAnswerName,sub.order,sub.idQuestion,"","","","","",it.question.questionName,it.question.objectType,false,false,it.question.order.toInt().toString(),it.question.required,""))
                                                }
                                            }
                                        }
                                        Either.Right(true)
                                    }
                                    else {
                                        Either.Left(Failure.DefaultError(body.message))
                                    }
                                }?: Either.Left(Failure.ServerError)
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

        override fun getCompanies(): Either<Failure, List<Company>> {
            return try {
                Either.Right(parametersDataSource.getCompanies(prefs.idUser?:"").map { it.toView() })
            }catch (e:Exception){
                Either.Left(Failure.DefaultError(e.message!!))
            }
        }

        override fun setPV(idCompany: String): Either<Failure, Boolean> {
            return when (networkHandler.isConnected) {
                true ->{
                    try {
                        val response = service.getSchedule("Bearer ${prefs.token!!}", idCompany).execute()
                        when (response.isSuccessful) {
                            true -> {
                                response.body()?.let { body ->
                                    if(body.success) {
                                        Log.d("setPV", body.data.toString())
                                        body.data.map {
                                            it.pv?.map { pv ->
                                                parametersDataSource.insertPV(pv.toModel(it.id?:"",it.userAssigned,pv.corp!!))
                                            }
                                        }
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

        override fun getPV(company:String): Either<Failure, List<Schedule>> {
            Log.d("getPv",company.toString())
            Log.d("idUser",prefs.idUser.toString())
            Log.d("val", parametersDataSource.getPV(company,prefs.idUser?:"").isNotEmpty().toString())
            return try {
                if (parametersDataSource.getPV(company,prefs.idUser?:"").isNotEmpty())
                    Either.Right(parametersDataSource.getPV(company,prefs.idUser?:"").map { it.toView() })
                else
                     Either.Right(emptyList())
            }catch (e:Exception){
                Either.Left(Failure.DefaultError(e.message!!))
            }
        }

        override fun getLanguage(): Either<Failure, List<TranslateItem>> {
            try {
                when (networkHandler.isConnected) {
                    true -> {
                        val response = service.getLanguage().execute()
                        if (response.isSuccessful) {
                            response.body()?.let {
                                for (element in it.data) {
                                    val json = Gson().toJson(
                                        element.translate,
                                        object : TypeToken<List<String>>() {}.type
                                    )
                                    parametersDataSource.insertTranslateItem(element.toModel(json))
                                }
                                translateObject.setInstance(it.data)
                                translateObject.LANGUAGE = prefs.language!!.toInt()
                                Log.d("prefs",translateObject.LANGUAGE .toString())
                                return Either.Right(it.data)
                            }
                        }
                        return Either.Left(Failure.MessageEmptyError("Ha ocurrido para descargar los idiomas"))

                    }
                    false -> {
                        return Either.Right(getTranslateObjectSaved())
                    }
                }
            } catch (e: Exception) {
                return Either.Left(Failure.DefaultError(e.message!!))
            }
        }

        private fun getTranslateObjectSaved():List<TranslateItem>{
            val dataSaved = parametersDataSource.getTranslate()
            val arrayTranslateItem : ArrayList<TranslateItem> = arrayListOf()
            for (element in dataSaved) {
                arrayTranslateItem.add(
                    TranslateItem(
                        element.id,
                        Gson()
                            .fromJson(element.translate, Array<String>::class.java)
                            .toList()
                    )
                )
            }
            translateObject.LANGUAGE = prefs.language!!.toInt()
            translateObject.setInstance(arrayTranslateItem)
            Log.d("translate", arrayTranslateItem.toString())
            return arrayTranslateItem
        }
        override fun setLanguage(language: Int): Either<Failure, Boolean> {
            return try{
                translateObject.LANGUAGE = language
                prefs.language = language
                Either.Right(true)
            }catch (e : Exception){
                Either.Left(Failure.DefaultError(e.message!!))
            }
        }

        override fun getTranslate(): Either<Failure, List<TranslateItem>> {
            try {
                val response = parametersDataSource.getTranslate()
                Log.d("responseGetTranslate",response.toString())
                val arrayTranslateItem  = ArrayList<TranslateItem>(emptyList())
                for(element in response){
                    val arrayParsed = Gson().fromJson(element.translate,Array<String>::class.java)
                    arrayTranslateItem.add(TranslateItem(element.id,arrayParsed.toList()))
                }
                translateObject.LANGUAGE = prefs.language!!.toInt()
                return Either.Right(arrayTranslateItem)
            }catch (e : Exception){
                return Either.Left(Failure.DefaultError(e.message!!))
            }
        }


    }
}