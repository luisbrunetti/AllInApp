package com.tawa.allinapp.features.reports.sku

import com.tawa.allinapp.core.interactor.UseCase
import com.tawa.allinapp.data.repository.ReportsRepository
import com.tawa.allinapp.features.init.usecase.GetIdCompany
import com.tawa.allinapp.models.Lines
import com.tawa.allinapp.models.SkuDetail
import javax.inject.Inject


class AddSku
@Inject constructor(private val reportsRepository: ReportsRepository) : UseCase<Boolean, AddSku.Params>() {

    override suspend fun run(params: Params) = reportsRepository.addSku(params.idReportPv,params.idPv,params.idCompany,params.lines)
    data class Params(val idReportPv:String,val idPv:String,val idCompany: String,val lines:List<Lines>)
}