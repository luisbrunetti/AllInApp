package com.tawa.allinapp.features.movies.usecase

import com.tawa.allinapp.core.interactor.UseCase
import com.tawa.allinapp.data.repository.MoviesRepository
import com.tawa.allinapp.features.movies.models.MovieDetail
import javax.inject.Inject

class GetMovieDetail
@Inject constructor(private val moviesRepository: MoviesRepository) : UseCase<MovieDetail, GetMovieDetail.Params>() {

    override suspend fun run(params: Params) = moviesRepository.movieDetails(params.id)

    data class Params(val id: Int)
}
