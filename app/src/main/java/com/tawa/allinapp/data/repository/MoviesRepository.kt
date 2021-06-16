package com.tawa.allinapp.data.repository

import com.tawa.allinapp.core.functional.Either
import com.tawa.allinapp.core.functional.Failure
import com.tawa.allinapp.core.functional.NetworkHandler
import com.tawa.allinapp.data.local.Prefs
import com.tawa.allinapp.data.local.datasource.MovieDataSource
import com.tawa.allinapp.data.remote.MovieDetailEntity
import com.tawa.allinapp.data.remote.service.MoviesService
import com.tawa.allinapp.features.movies.models.Movie
import com.tawa.allinapp.features.movies.models.MovieDetail
import retrofit2.Call
import javax.inject.Inject

interface MoviesRepository {
    fun movies(): Either<Failure, List<Movie>>
    fun movieDetails(movieId: Int): Either<Failure, MovieDetail>
    fun setData(): Either<Failure, Boolean>

    class Network
    @Inject constructor(private val networkHandler: NetworkHandler,
                        private val prefs: Prefs,
                        private val service: MoviesService,
                        private val movieDataSource: MovieDataSource
    ) : MoviesRepository {

        override fun movies(): Either<Failure, List<Movie>> {
            return Either.Right(movieDataSource.getMovies())
        }

        override fun setData(): Either<Failure, Boolean> {
            return when (networkHandler.isConnected) {
                true ->{
                    try {
                        val response = service.getMovies().execute()
                        when (response.isSuccessful) {
                            true -> {
                                response.body()?.let {
                                    prefs.name = "Rodrigo"
                                    it.map { movieEntity ->
                                        movieDataSource.insertMovies(movieEntity.toMovie())
                                    }
                                    Either.Right(true)
                                }?: Either.Left(Failure.DefaultError(""))
                            }
                            false -> Either.Left(Failure.ServerError)
                        }
                    } catch (e: Exception) {
                        Either.Left(Failure.DefaultError(e.message!!))
                    }
                }
                false -> Either.Left(Failure.NetworkConnection)
            }
        }

        override fun movieDetails(movieId: Int): Either<Failure, MovieDetail> {
            return when (networkHandler.isConnected) {
                true -> request(service.getMovieDetail(movieId), {
                    it.toMovieDetail()
                }, MovieDetailEntity.Response())
                false -> Either.Left(Failure.NetworkConnection)
            }
        }

        private fun <T, R> request(call: Call<T>, transform: (T) -> R, default: T): Either<Failure, R> {
            return try {
                val response = call.execute()
                when (response.isSuccessful) {
                    true -> Either.Right(transform((response.body() ?: default)))
                    false -> Either.Left(Failure.ServerError)
                }
            } catch (exception: Throwable) {
                Either.Left(Failure.ServerError)
            }
        }
    }
}