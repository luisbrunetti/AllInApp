package com.tawa.allinapp.core.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.tawa.allinapp.BuildConfig
import com.tawa.allinapp.data.local.AllInAppDatabase
import com.tawa.allinapp.data.local.Prefs
import com.tawa.allinapp.data.local.dao.*
import com.tawa.allinapp.data.repository.*
import com.tawa.allinapp.models.Question
import com.tawa.allinapp.models.Report
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
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
    @Provides @Singleton fun providesCheckDao(demoDatabase:AllInAppDatabase) : CheckDao { return demoDatabase.checkDao() }
    @Provides @Singleton fun providesReportDao(demoDatabase:AllInAppDatabase) : ReportsDao { return demoDatabase.reportsDao() }
    @Provides @Singleton fun providesQuestionDao(demoDatabase:AllInAppDatabase) : QuestionsDao { return demoDatabase.questionsDao() }

    @Provides @Singleton fun provideMoviesRepository(dataSource: MoviesRepository.Network): MoviesRepository = dataSource
    @Provides @Singleton fun provideAuthRepository(dataSource: AuthRepository.Network): AuthRepository = dataSource
    @Provides @Singleton fun provideParametersRepository(dataSource: ParametersRepository.Network): ParametersRepository = dataSource
    @Provides @Singleton fun provideCheckRepository(dataSource: CheckRepository.Network): CheckRepository = dataSource
    @Provides @Singleton fun provideReportRepository(dataSource: ReportsRepository.Network): ReportsRepository = dataSource
    @Provides @Singleton fun provideQuestionRepository(dataSource: QuestionsRepository.Network): QuestionsRepository = dataSource
    @Provides @Singleton fun provideRoutesRepository(dataSource: RoutesRepository.Network): RoutesRepository = dataSource
    @Provides @Singleton fun provideDashboardRepository(dataSource: DashboardRepository.Network): DashboardRepository = dataSource

}