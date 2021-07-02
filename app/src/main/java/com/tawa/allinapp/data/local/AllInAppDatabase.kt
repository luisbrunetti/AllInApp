package com.tawa.allinapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.tawa.allinapp.data.local.dao.*
import com.tawa.allinapp.data.local.models.*
import com.tawa.allinapp.features.movies.models.Movie

@Database(entities = [
    Movie::class,
    CompanyModel::class,
    PVModel::class,
    CheckModel::class,
    ReportModel::class,
    QuestionModel::class,
    AnswerModel::class,
],version = 1)
abstract class AllInAppDatabase: RoomDatabase(){
    abstract fun movieDao(): MovieDao
    abstract fun parametersDao(): ParametersDao
    abstract fun checkDao(): CheckDao
    abstract fun reportsDao(): ReportsDao
    abstract fun questionsDao():QuestionsDao
}