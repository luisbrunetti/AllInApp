package com.tawa.allinapp.features.reports.standard

import com.tawa.allinapp.core.interactor.UseCase
import com.tawa.allinapp.data.repository.QuestionsRepository
import com.tawa.allinapp.models.Question
import javax.inject.Inject

class GetQuestions
@Inject constructor(private val questionsRepository: QuestionsRepository) : UseCase<List<Question>, UseCase.None>() {

    override suspend fun run(params: None) = questionsRepository.getQuestions()

}