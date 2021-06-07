package com.tawa.allinapp.data.remote

import com.tawa.allinapp.features.movies.models.MovieDetail

class MovieDetailEntity {
    data class Response(
        private val id: Int=0,
        private val title: String="",
        private val poster: String="",
        private val summary: String="",
        private val cast: String="",
        private val director: String="",
        private val year: Int=0,
        private val trailer: String="")
    {
        fun toMovieDetail() =
            MovieDetail(
                id,
                title,
                poster,
                summary,
                cast,
                director,
                year,
                trailer
            )
    }
}