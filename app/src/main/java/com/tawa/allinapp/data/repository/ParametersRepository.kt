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
    fun getLanguage(): Either<Failure,Translate>
    fun getTranslate(): Either<Failure,Translate>
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

        override fun getLanguage(): Either<Failure, Translate> {
            try {
                val retrofit = provideRetrofit()
                val response = retrofit.getLanguage().execute()
                if (response.isSuccessful) {
                    response.body()?.let {
                        Log.d("response", it.toString())
                        /*for(element in it.data.arrayTranslate){
                            val string = Gson().toJson(element.translate, object: TypeToken<List<String>>() {}.type)
                            Log.d("string",string.toString())
                            parametersDataSource.insertTranslateItem(element.toModel(string))
                        }*/
                        Log.d("posijtion",prefs.language.toString())
                        translateObject.setInstance(it.data)
                        translateObject.LANGUAGE = prefs.language!!.toInt()
                        return Either.Right(it.data)
                    } ?: Either.Right(Translate(emptyList()))
                }
                return Either.Right(Translate(emptyList()))
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

        override fun getTranslate(): Either<Failure, Translate> {
            try {
                val retrofit = provideRetrofit()
                val response = retrofit.getLanguage().execute()
                if(response.isSuccessful){
                    response.body()?.let {
                        Log.d("response",it.toString())

                        for(element in it.data.arrayTranslate){
                            val string = Gson().toJson(element.translate, object: TypeToken<List<String>>() {}.type)
                            Log.d("string",string.toString())
                            parametersDataSource.insertTranslateItem(element.toModel(string))
                        }
                        return Either.Right(it.data)
                    }?: Either.Left(Failure.DefaultError(""))
                }
                return Either.Left(Failure.DefaultError(""))
            }catch (e : Exception){
                return Either.Left(Failure.DefaultError(e.message!!))
            }
        }


        /*override fun getLanguageByXml(xmlName: String): Either<Failure, List<com.tawa.allinapp.models.Language>> {
            return try{
                Either.Right(parametersDataSource.getLanguageByXml(xmlName).map { it.toView() })
            }catch (e : Exception){
                Either.Left(Failure.DefaultError(e.message))
            }
        }*/
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