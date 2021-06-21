package com.tawa.allinapp.core.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.tawa.allinapp.BuildConfig
import com.tawa.allinapp.data.local.AllInAppDatabase
import com.tawa.allinapp.data.local.Prefs
import com.tawa.allinapp.data.local.dao.MovieDao
import com.tawa.allinapp.data.local.dao.ParametersDao
import com.tawa.allinapp.data.repository.AuthRepository
import com.tawa.allinapp.data.repository.MoviesRepository
import com.tawa.allinapp.data.repository.ParametersRepository
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class ApplicationModule(private val application: Application){

    @Provides
    @Singleton
    fun provideApplicationContext(): Context = application

    @Provides
    @Singleton
    fun providePreferences() = Prefs(application)

    @Provides
    @Singleton
    fun provideRoom(): AllInAppDatabase {
        return Room.databaseBuilder(application, AllInAppDatabase::class.java, "app").build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(createClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun createClient(): OkHttpClient {
        val okHttpClientBuilder: OkHttpClient.Builder = OkHttpClient.Builder()
        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC)
            okHttpClientBuilder.addInterceptor(loggingInterceptor)
        }
        return okHttpClientBuilder.build()
    }

    @Provides @Singleton fun providesMovieDao(demoDatabase:AllInAppDatabase) : MovieDao { return demoDatabase.movieDao() }
    @Provides @Singleton fun providesParametersDao(demoDatabase:AllInAppDatabase) : ParametersDao { return demoDatabase.parametersDao() }

    @Provides @Singleton fun provideMoviesRepository(dataSource: MoviesRepository.Network): MoviesRepository = dataSource
    @Provides @Singleton fun provideAuthRepository(dataSource: AuthRepository.Network): AuthRepository = dataSource
    @Provides @Singleton fun provideParametersRepository(dataSource: ParametersRepository.Network): ParametersRepository = dataSource

}