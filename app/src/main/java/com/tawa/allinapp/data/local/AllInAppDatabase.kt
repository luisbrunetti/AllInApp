package com.tawa.allinapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.tawa.allinapp.data.local.dao.MovieDao
import com.tawa.allinapp.features.movies.models.Movie

@Database(entities = [Movie::class],version = 2)
abstract class AllInAppDatabase: RoomDatabase(){
    abstract fun movieDao(): MovieDao
}