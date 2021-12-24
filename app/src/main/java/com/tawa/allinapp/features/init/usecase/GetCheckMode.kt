package com.tawa.allinapp.features.init.usecase

import com.tawa.allinapp.core.interactor.UseCase
import com.tawa.allinapp.data.repository.CheckRepository
import com.tawa.allinapp.models.CheckInHistoryView
import javax.inject.Inject

class GetCheckMode
@Inject constructor(private val checkRepository: CheckRepository) : UseCase<CheckInHistoryView, UseCase.None>() {

    override suspend fun run(params: None) = checkRepository.getCheckMode()

}