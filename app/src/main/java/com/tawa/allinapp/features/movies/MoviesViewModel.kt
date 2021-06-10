package com.tawa.allinapp.features.movies

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tawa.allinapp.core.interactor.UseCase
import com.tawa.allinapp.core.platform.BaseViewModel
import com.tawa.allinapp.features.movies.models.Movie
import com.tawa.allinapp.features.movies.models.MovieDetail
import com.tawa.allinapp.features.movies.ui.MovieDetailView
import com.tawa.allinapp.features.movies.usecase.GetMovieDetail
import com.tawa.allinapp.features.movies.usecase.GetMovies
import com.tawa.allinapp.features.movies.usecase.SetData
import javax.inject.Inject

class MoviesViewModel
@Inject constructor(
    private val getMovies: GetMovies,
    private val getMovieDetail: GetMovieDetail,
    private val setData: SetData
) : BaseViewModel() {

    private val _movies = MutableLiveData<List<MovieView>>()
    val movies: LiveData<List<MovieView>>
        get()= _movies

    //var movies: MutableLiveData<List<MovieView>> = MutableLiveData()
    var movieDetail: MutableLiveData<MovieDetailView> = MutableLiveData()
    var sData: MutableLiveData<Boolean> = MutableLiveData()

    fun loadData() = setData(UseCase.None()) { it.either(::handleFailure, ::handleData) }

    private fun handleData(ok: Boolean) {
        this.sData.value = ok
    }

    fun loadMovies() = getMovies(UseCase.None()) { it.either(::handleFailure, ::handleMovieList) }

    private fun handleMovieList(movies: List<Movie>) {
        this._movies.value = movies.map { MovieView(it.id, it.poster) }
    }

    fun loadMovie(movieId:Int) = getMovieDetail(GetMovieDetail.Params(movieId)) { it.either(::handleFailure, ::handleMovieDetail) }

    private fun handleMovieDetail(movie: MovieDetail) {
        this.movieDetail.value = MovieDetailView(
            movie.id, movie.title, movie.poster, movie.summary, movie.cast, movie.director, movie.year, movie.trailer
        )
    }
}