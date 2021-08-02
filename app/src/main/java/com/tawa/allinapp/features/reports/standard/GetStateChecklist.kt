package com.tawa.allinapp.features.reports.standard

import com.tawa.allinapp.core.interactor.UseCase
import com.tawa.allinapp.data.repository.QuestionsRepository
import javax.inject.Inject

class GetStateChecklist
@Inject constructor(private val questionsRepository: QuestionsRepository) : UseCase<ArrayList<Boolean>, UseCase.None>() {

    override suspend fun run(params: None) = questionsRepository.getStateChecklist()
}