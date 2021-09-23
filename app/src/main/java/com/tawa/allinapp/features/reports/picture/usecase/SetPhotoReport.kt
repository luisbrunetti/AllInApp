package com.tawa.allinapp.features.reports.picture.usecase

import com.tawa.allinapp.core.interactor.UseCase
import com.tawa.allinapp.data.repository.ReportsRepository
import com.tawa.allinapp.models.PhotoReport
import javax.inject.Inject


class SetPhotoReport
@Inject constructor(private val reportsRepository: ReportsRepository) : UseCase<Boolean, SetPhotoReport.Params>() {

    override suspend fun run(params: Params) = reportsRepository.saveLocalPhotoReport(params.report, params.state,params.type)

    class Params(val report: PhotoReport?, val state:String,val type:String)

}