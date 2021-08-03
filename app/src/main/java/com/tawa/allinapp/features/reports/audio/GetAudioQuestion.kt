package com.tawa.allinapp.features.reports.audio

import com.tawa.allinapp.core.interactor.UseCase
import com.tawa.allinapp.data.repository.QuestionsRepository
import com.tawa.allinapp.data.repository.ReportsRepository
import com.tawa.allinapp.models.AudioReport
import com.tawa.allinapp.models.PhotoReport
import com.tawa.allinapp.models.Question
import javax.inject.Inject


class GetAudioQuestion
@Inject constructor(private val  questionsRepository: QuestionsRepository) : UseCase<List<Question>, UseCase.None>() {

    override suspend fun run(params: None) = questionsRepository.getAudioQuestions()

}