package com.tawa.allinapp.features.reports.sku

import com.tawa.allinapp.core.interactor.UseCase
import com.tawa.allinapp.data.repository.ReportsRepository
import com.tawa.allinapp.models.Sku
import com.tawa.allinapp.models.SkuDetail
import javax.inject.Inject


class GetTypeSku
@Inject constructor(private val reportsRepository: ReportsRepository) : UseCase<String, UseCase.None>() {

    override suspend fun run(params: None) = reportsRepository.getTypeSku()

}