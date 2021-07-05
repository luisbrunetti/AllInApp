package com.tawa.allinapp.features.reports.standard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tawa.allinapp.core.interactor.UseCase
import com.tawa.allinapp.core.platform.BaseViewModel
import com.tawa.allinapp.models.Answer
import com.tawa.allinapp.models.Question
import javax.inject.Inject

class CheckListViewModel
@Inject constructor (
    private val getQuestions: GetQuestions,
    private val getAnswers: GetAnswers
    ):BaseViewModel() {

    private val _text = MutableLiveData<String>("HOLAAAAAAA")
    val text: LiveData<String>
        get()= _text

    private val _questions = MutableLiveData<List<Question>>()
    val questions: LiveData<List<Question>>
        get()= _questions

    private val _nameQuestion = MutableLiveData<String>("")
    val nameQuestion: LiveData<String>
        get()= _nameQuestion

    private val _order = MutableLiveData<Int>(0)
    val order: LiveData<Int>
        get()= _order

    private val _order1 = MutableLiveData<Int>(0)
    val order1: LiveData<Int>
        get()= _order1

    private val _answersRadio = MutableLiveData<List<Answer>>()
    val answersRadio: LiveData<List<Answer>>
        get()= _answersRadio

    private val _answersCheck = MutableLiveData<List<Answer>>()
    val answersCheck: LiveData<List<Answer>>
        get()= _answersCheck

    fun getQuestions() = getQuestions(UseCase.None()) { it.either(::handleFailure, ::handleQuestions) }

    private fun handleQuestions(questions : List<Question>) {
        _questions.value = questions
    }

    fun getAnswersRadio(idQuestion:String,nameQ: String,order:Int) = getAnswers(GetAnswers.Params(idQuestion)) {
        _nameQuestion.value = nameQ
        _order.value = order
        it.either(::handleFailure, ::handleAnswersRadio) }

    private fun handleAnswersRadio(answers : List<Answer>) {
        _answersRadio.value = answers
    }

    fun getAnswersCheck(idQuestion:String,nameQ: String,order: Int) = getAnswers(GetAnswers.Params(idQuestion)) {
        _nameQuestion.value = nameQ
        _order1.value = order
        it.either(::handleFailure, ::handleAnswersCheck) }

    private fun handleAnswersCheck(answers : List<Answer>) {
        _answersCheck.value = answers
    }
}