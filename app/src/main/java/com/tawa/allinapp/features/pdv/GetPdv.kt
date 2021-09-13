package com.tawa.allinapp.features.pdv

import com.tawa.allinapp.core.interactor.UseCase
import com.tawa.allinapp.data.repository.CheckRepository
import com.tawa.allinapp.data.repository.PdvRepository
import com.tawa.allinapp.models.Pdv
import javax.inject.Inject

class GetPdv
@Inject constructor(private val pdvRepository: PdvRepository) : UseCase<Pdv, UseCase.None>() {

    override suspend fun run(params: None) = pdvRepository.getPdv()

}