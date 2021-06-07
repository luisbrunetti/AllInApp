package com.tawa.allinapp.features.movies.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tawa.allinapp.R
import com.tawa.allinapp.core.extensions.failure
import com.tawa.allinapp.core.extensions.observe
import com.tawa.allinapp.core.extensions.viewModel
import com.tawa.allinapp.core.platform.BaseFragment
import com.tawa.allinapp.databinding.FragmentMoviesBinding
import com.tawa.allinapp.features.movies.MoviesViewModel
import javax.inject.Inject


class MoviesFragment : BaseFragment() {

    @Inject
    lateinit var moviesAdapter: MoviesAdapter

    private lateinit var moviesViewModel: MoviesViewModel

    private lateinit var mBinding: FragmentMoviesBinding

    override fun layoutId() = R.layout.fragment_movies

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mBinding = FragmentMoviesBinding.inflate(inflater)
        return mBinding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)

        moviesViewModel = viewModel(viewModelFactory) {
            observe(movies, {
                it?.let {
                    moviesAdapter.collection = it
                }
            })
            observe(movieDetail,{
                it?.let {

                }
            })
            observe(sData,{
                it?.let {
                    moviesViewModel.loadMovies()
                }
            })
            failure(failure, {
                it?.let {

                }
            })
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.movieList.layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        mBinding.movieList.adapter = moviesAdapter

        moviesViewModel.loadData()

        //moviesViewModel.loadMovie(38001)

    }
}