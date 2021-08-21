package com.tawa.allinapp.features.reports

import com.tawa.allinapp.core.interactor.UseCase
import com.tawa.allinapp.data.repository.ReportsRepository
import javax.inject.Inject


class GetCountSku
@Inject constructor(private val reportsRepository: ReportsRepository) : UseCase<Int, UseCase.None>() {

    override suspend fun run(params: None) = reportsRepository.getCountSku()
}