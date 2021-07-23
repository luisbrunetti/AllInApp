package com.tawa.allinapp.features.reports.standard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tawa.allinapp.core.interactor.UseCase
import com.tawa.allinapp.core.platform.BaseViewModel
import com.tawa.allinapp.features.init.usecase.SetCheckIn
import com.tawa.allinapp.models.Answer
import com.tawa.allinapp.models.Question
import javax.inject.Inject

class CheckListViewModel
@Inject constructor (
    private val getQuestions: GetQuestions,
    private val getAnswers: GetAnswers,
    private val setReadyAnswers: SetReadyAnswers
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

    private val _orderRadio = MutableLiveData<Int>(0)
    val orderRadio: LiveData<Int>
        get()= _orderRadio

    private val _orderCheckBox = MutableLiveData<Int>(0)
    val orderCheckBox: LiveData<Int>
        get()= _orderCheckBox

    private val _orderInput = MutableLiveData<Int>(0)
    val orderInput: LiveData<Int>
        get()= _orderInput

    private val _answersRadio = MutableLiveData<List<Answer>>()
    val answersRadio: LiveData<List<Answer>>
        get()= _answersRadio

    private val _answersAll = MutableLiveData<List<Answer>>()
    val answersAll: LiveData<List<Answer>>
        get()= _answersAll

    private val _answersInput = MutableLiveData<List<Answer>>()
    val answersInput: LiveData<List<Answer>>
        get()= _answersInput

    private val _stateRadio = MutableLiveData<Boolean>(false)
    val stateRadio: LiveData<Boolean>
        get()= _stateRadio

    private val _stateCheck = MutableLiveData<Boolean>(false)
    val stateCheck: LiveData<Boolean>
        get()= _stateCheck

    private val _stateInput = MutableLiveData<Boolean>(false)
    val stateInput: LiveData<Boolean>
        get()= _stateInput

    private val _answersCheck = MutableLiveData<List<Answer>>()
    val answersCheck: LiveData<List<Answer>>
        get()= _answersCheck

    private val _successReadyAnswers = MutableLiveData<Boolean>(false)
    val successReadyAnswers: LiveData<Boolean>
        get()= _successReadyAnswers

    fun getQuestions() = getQuestions(UseCase.None()) { it.either(::handleFailure, ::handleQuestions) }

    private fun handleQuestions(questions : List<Question>) {
        _questions.value = questions
    }

    fun getAnswersRadio(idQuestion:String,nameQ: String,order:Int) = getAnswers(GetAnswers.Params(idQuestion)) {
        _nameQuestion.value = nameQ
        _orderRadio.value = order
        it.either(::handleFailure, ::handleAnswersRadio) }

    private fun handleAnswersRadio(answers : List<Answer>) {
        _answersRadio.value = answers
    }

    fun getAnswersAll(idQuestion:String) = getAnswers(GetAnswers.Params(idQuestion)) {
        it.either(::handleFailure, ::handleAnswersAll) }

    private fun handleAnswersAll(answers : List<Answer>) {
        _answersAll.value = answers
    }

    fun getAnswersCheck(idQuestion:String,nameQ: String,order: Int) = getAnswers(GetAnswers.Params(idQuestion)) {
        _nameQuestion.value = nameQ
        _orderCheckBox.value = order
        it.either(::handleFailure, ::handleAnswersCheck) }

    private fun handleAnswersCheck(answers : List<Answer>) {
        _answersCheck.value = answers
    }

    fun getAnswersInput(idQuestion:String,nameQ: String,order: Int) = getAnswers(GetAnswers.Params(idQuestion)) {
        _nameQuestion.value = nameQ
        _orderInput.value = order
        it.either(::handleFailure, ::handleAnswersInput) }

    private fun handleAnswersInput(answers : List<Answer>) {
        _answersInput.value = answers
    }

    fun startRadio(){
        _stateRadio.value= true
    }

    fun startCheck(){
        _stateCheck.value= true
    }

    fun startInput(){
        _stateInput.value= true
    }

    fun setReadyAnswers(idQuestion: String,nameQuestion: String,idAnswer:String,nameAnswer: String) {
        setReadyAnswers(SetReadyAnswers.Params(0,idQuestion,nameQuestion,idAnswer,nameAnswer)) {
            it.either(::handleFailure, ::handleReadyAnswers)
        }
    }

    private fun handleReadyAnswers(success: Boolean) {
        this._successReadyAnswers.value = success
    }
}