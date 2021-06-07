package com.tawa.allinapp.data.remote

import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MoviesService
@Inject constructor(retrofit: Retrofit) : MoviesApi {
    private val moviesApi by lazy { retrofit.create(MoviesApi::class.java) }

    override fun getMovies() = moviesApi.getMovies()
    override fun getMovieDetail(movieId: Int) = moviesApi.getMovieDetail(movieId)
}
