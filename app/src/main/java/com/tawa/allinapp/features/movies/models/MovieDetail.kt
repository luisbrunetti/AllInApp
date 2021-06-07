package com.tawa.allinapp.features.movies.models

data class MovieDetail(val id: Int=0,
                       val title: String="",
                       val poster: String="",
                       val summary: String="",
                       val cast: String="",
                       val director: String="",
                       val year: Int=0,
                       val trailer: String="")