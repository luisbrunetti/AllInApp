package com.tawa.allinapp.data.remote

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

internal interface MoviesApi {
    companion object {
        private const val PARAM_MOVIE_ID = "movieId"
        private const val MOVIES = "movies.json"
        private const val MOVIE_DETAILS = "movie_0{$PARAM_MOVIE_ID}.json"
    }

    @GET(MOVIES) fun getMovies(): Call<List<MovieEntity.Response>>
    @GET(MOVIE_DETAILS) fun getMovieDetail(@Path(PARAM_MOVIE_ID) movieId: Int): Call<MovieDetailEntity.Response>
}