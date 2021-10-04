package com.tawa.allinapp.features.init.usecase

import com.tawa.allinapp.core.interactor.UseCase
import com.tawa.allinapp.data.repository.CheckRepository
import javax.inject.Inject

class DeletePv
@Inject constructor(private val checkrepository: CheckRepository) : UseCase<Boolean, UseCase.None>() {

    override suspend fun run(params: None) = checkrepository.deleteIdPvNPvName()

}