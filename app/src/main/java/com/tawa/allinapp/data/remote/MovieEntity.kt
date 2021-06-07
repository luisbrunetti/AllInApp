package com.tawa.allinapp.data.remote

import com.google.gson.annotations.SerializedName
import com.tawa.allinapp.features.movies.models.Movie

class MovieEntity {
    data class Response(
        @SerializedName("id") val id: Int,
        @SerializedName("poster") val poster: String
    ) {
        fun toMovie() = Movie(id, poster)
    }
}