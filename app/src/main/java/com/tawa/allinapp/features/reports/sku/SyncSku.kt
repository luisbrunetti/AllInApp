package com.tawa.allinapp.features.reports.sku

import com.tawa.allinapp.core.interactor.UseCase
import com.tawa.allinapp.data.repository.ReportsRepository
import com.tawa.allinapp.features.init.usecase.GetIdCompany
import com.tawa.allinapp.models.Lines
import com.tawa.allinapp.models.SkuDetail
import javax.inject.Inject


class SyncSku
@Inject constructor(private val reportsRepository: ReportsRepository) : UseCase<Boolean, UseCase.None>() {

    override suspend fun run(params: None) = reportsRepository.syncSku()

}