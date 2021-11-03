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
import com.tawa.allinapp.data.remote.api.ParametersApi
import com.tawa.allinapp.data.remote.service.ParametersService
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
    fun getCompanies(): Either<Failure,List<Company>>
    fun setPV(idCompany: String): Either<Failure, Boolean>
    fun getPV(company:String): Either<Failure, List<Schedule>>
    fun setLanguage(language:Int) : Either<Failure, Boolean>
    fun getLanguage(): Either<Failure,List<TranslateItem>>
    fun getTranslate(): Either<Failure,List<TranslateItem>>
    //fun getLanguageByXml(xmlName : String): Either<Failure, List<com.tawa.allinapp.models.Language>>
    class Network
    @Inject constructor(private val networkHandler: NetworkHandler,
                        private val parametersDataSource: ParametersDataSource,
                        private val prefs: Prefs,
                        private val service: ParametersService,
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
                                    if(body.success) {
                                        body.data.map {
                                            Log.d("setCompanies",it.id+ "\n token -> "+ prefs.token.toString())
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
                        val retrofit = provideRetrofit()
                        val response = service.getLanguage().execute()
                        if (response.isSuccessful) {
                            response.body()?.let {
                                //Log.d("response", it.toString())
                                for (element in it.data) {
                                    val json = Gson().toJson(
                                        element.translate,
                                        object : TypeToken<List<String>>() {}.type
                                    )
                                    parametersDataSource.insertTranslateItem(element.toModel(json))
                                }
                                Log.d("posijtion", prefs.language.toString())
                                translateObject.setInstance(it.data)
                                translateObject.LANGUAGE = prefs.language!!.toInt()
                                return Either.Right(it.data)
                            }
                        }
                        return Either.Right(emptyList())
                    }
                    false -> {
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
                        return Either.Right(arrayTranslateItem)
                    }
                }
            } catch (e: Exception) {
                return Either.Left(Failure.DefaultError(e.message!!))
            }
        }
        override fun setLanguage(language: Int): Either<Failure, Boolean> {
            return try{
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
                return Either.Right(arrayTranslateItem)
            }catch (e : Exception){
                return Either.Left(Failure.DefaultError(e.message!!))
            }
        }
        fun provideRetrofit(): ParametersApi {
            return Retrofit.Builder()
                .baseUrl("http://run.mocky.io/v3/")
                .client(createClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ParametersApi::class.java)
        }
        private fun createClient(): OkHttpClient {
            val okHttpClientBuilder: OkHttpClient.Builder = OkHttpClient.Builder()
            if (BuildConfig.DEBUG) {
                val loggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC)
                okHttpClientBuilder.addInterceptor(loggingInterceptor)
            }
            return okHttpClientBuilder.build()
        }



    }
}