package com.tawa.allinapp.data.local.datasource

import com.tawa.allinapp.data.local.dao.MovieDao
import com.tawa.allinapp.features.movies.models.Movie
import javax.inject.Inject

class MovieDataSource @Inject constructor(private val movieDao: MovieDao){
    fun insertMovies(movie: Movie){
        movieDao.insertMovie(movie)
    }
    fun getMovies():List<Movie> {
        return movieDao.getMovies()
    }
}
