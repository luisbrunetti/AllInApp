package com.tawa.allinapp.features.movies.usecase

import com.tawa.allinapp.core.interactor.UseCase
import com.tawa.allinapp.data.repository.MoviesRepository
import javax.inject.Inject

class SetData
@Inject constructor(private val moviesRepository: MoviesRepository) : UseCase<Boolean, UseCase.None>() {

    override suspend fun run(params: None) = moviesRepository.setData()

}