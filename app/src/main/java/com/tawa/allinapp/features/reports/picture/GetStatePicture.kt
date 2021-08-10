package com.tawa.allinapp.features.reports.picture

import com.tawa.allinapp.core.interactor.UseCase
import com.tawa.allinapp.data.repository.ReportsRepository
import javax.inject.Inject

class GetStatePicture
@Inject constructor(private val reportsRepository: ReportsRepository) : UseCase<String, GetStatePicture.Params>() {

    override suspend fun run(params: Params) = reportsRepository.getStatePhotoReport(params.idPv)

    class Params(val idPv:String)

}