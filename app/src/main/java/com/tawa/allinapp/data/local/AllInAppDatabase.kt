package com.tawa.allinapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.tawa.allinapp.data.local.dao.MovieDao
import com.tawa.allinapp.data.local.dao.ParametersDao
import com.tawa.allinapp.data.local.models.CheckinModel
import com.tawa.allinapp.data.local.models.CompanyModel
import com.tawa.allinapp.data.local.models.PVModel
import com.tawa.allinapp.features.movies.models.Movie

@Database(entities = [
    Movie::class,
    CompanyModel::class,
    PVModel::class,
    CheckinModel::class,
],version = 1)
abstract class AllInAppDatabase: RoomDatabase(){
    abstract fun movieDao(): MovieDao
    abstract fun parametersDao(): ParametersDao
}