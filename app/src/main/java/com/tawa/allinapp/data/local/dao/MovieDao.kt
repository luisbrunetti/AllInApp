package com.tawa.allinapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tawa.allinapp.features.movies.models.Movie

@Dao
interface MovieDao{
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertMovie(movie: Movie)

    @Query("SELECT * FROM movie")
    fun getMovies(): List<Movie>

    @Query("DELETE FROM movie")
    fun deleteMovie()
}