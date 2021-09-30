package com.tawa.allinapp.features.init.usecase

import com.tawa.allinapp.core.interactor.UseCase
import com.tawa.allinapp.data.repository.AuthRepository
import com.tawa.allinapp.data.repository.CheckRepository
import com.tawa.allinapp.data.repository.NotifyRepository
import com.tawa.allinapp.models.Notify
import javax.inject.Inject

class GetNotify
@Inject constructor(private val notifyRepository: NotifyRepository) : UseCase<List<Notify>, UseCase.None>() {

    override suspend fun run(params: None) = notifyRepository.getNotify()

}