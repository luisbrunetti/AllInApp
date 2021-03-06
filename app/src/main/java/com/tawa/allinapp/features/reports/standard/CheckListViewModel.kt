package com.tawa.allinapp.features.reports.standard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tawa.allinapp.core.platform.BaseViewModel
import com.tawa.allinapp.features.init.usecase.SyncStandardReports
import com.tawa.allinapp.models.Answer
import javax.inject.Inject

class CheckListViewModel
@Inject constructor (
    private val getQuestions: GetQuestions,
    private val getAnswers: GetAnswers,
    private val setReadyAnswers: SetReadyAnswers,
    private val updateAnswers: UpdateAnswers,
    private val updateState: UpdateState,
    private val getStateReport: GetStateReport,
    private val updateStateReport: UpdateStateReport,
    private val updateReportPv: UpdateReportPv,
    private val setAnswerPv: SetAnswerPv,
    private val syncStandardReports: SyncStandardReports
    ):BaseViewModel() {

    private val _text = MutableLiveData<String>("HOLAAAAAAA")
    val text: LiveData<String>
        get()= _text

    private val _questions = MutableLiveData<List<Answer>>()
    val questions: LiveData<List<Answer>>
        get()= _questions

    private val _nameQuestion = MutableLiveData<String>("")
    val nameQuestion: LiveData<String>
        get()= _nameQuestion

    private val _orderRadio = MutableLiveData<Int>(0)
    val orderRadio: LiveData<Int>
        get()= _orderRadio

    private val _orderGen = MutableLiveData<Int>(0)
    val orderGen: LiveData<Int>
        get()= _orderGen

    private val _orderCheckBox = MutableLiveData<Int>(0)
    val orderCheckBox: LiveData<Int>
        get()= _orderCheckBox

    private val _orderInput = MutableLiveData<Int>(0)
    val orderInput: LiveData<Int>
        get()= _orderInput

    private val _answersRadio = MutableLiveData<List<Answer>>()
    val answersRadio: LiveData<List<Answer>>
        get()= _answersRadio

    private val _answersGen = MutableLiveData<List<Answer>>()
    val answersGen: LiveData<List<Answer>>
        get()= _answersGen

    private val _answersAll = MutableLiveData<List<Answer>>()
    val answersAll: LiveData<List<Answer>>
        get()= _answersAll

    private val _answersInput = MutableLiveData<List<Answer>>()
    val answersInput: LiveData<List<Answer>>
        get()= _answersInput

    private val _answersPhoto = MutableLiveData<List<Answer>>()
    val answersPhoto: LiveData<List<Answer>>
        get()= _answersPhoto

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

    private val _successUpdateAnswers = MutableLiveData<Boolean>(false)
    val successUpdateAnswers: LiveData<Boolean>
        get()= _successUpdateAnswers

    private val _successUpdateState = MutableLiveData<Boolean>(false)
    val successUpdateState: LiveData<Boolean>
        get()= _successUpdateState

    private val _successSetReportPv = MutableLiveData<Boolean>(false)
    val successSetReportPv: LiveData<Boolean>
        get()= _successSetReportPv

    private val _successSetAnswerPv = MutableLiveData<Boolean>(false)
    val successSetAnswerPv: LiveData<Boolean>
        get()= _successSetAnswerPv

    private val _type = MutableLiveData<Int>(0)
    val type: LiveData<Int>
        get()= _type

    private val _successSyncReportStandard = MutableLiveData(false)
    val successSyncReportStandard : LiveData<Boolean>
        get() = _successSyncReportStandard


    private val _stateReport = MutableLiveData("")
    val stateReport: LiveData<String>
        get()= _stateReport

    private val _updateReportState = MutableLiveData<Boolean>(false)
    val updateReportState: LiveData<Boolean>
        get()= _updateReportState

    private val _errorMessage = MutableLiveData("")
    val errorMessage = _errorMessage

    fun setError(error:String){
        _errorMessage.value = error
    }



    fun getQuestions(idReport: String) = getQuestions(GetQuestions.Params(idReport)) { it.either(::handleFailure, ::handleQuestions) }

    private fun handleQuestions(questions : List<Answer>) {
        _questions.value = questions
    }



    init {
       // getStateCheckList(1)
    }

    fun getAnswersRadio(idQuestion:String,nameQ: String,order:Int) = getAnswers(GetAnswers.Params(idQuestion)) {
        _nameQuestion.value = nameQ
        _orderRadio.value = order
        it.either(::handleFailure, ::handleAnswersRadio) }

    private fun handleAnswersRadio(answers : List<Answer>) {
        _answersRadio.value = answers
    }

    fun getAnswersGen(idQuestion:String,nameQ: String,order:Int) = getAnswers(GetAnswers.Params(idQuestion)) {
        _nameQuestion.value = nameQ
        _orderGen.value = order
        it.either(::handleFailure, ::handleAnswersGen) }

    private fun handleAnswersGen(answers : List<Answer>) {
        _answersGen.value = answers
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

    fun getAnswersPhoto(idQuestion:String,nameQuestion: String) = getAnswers(GetAnswers.Params(idQuestion)) {
       _nameQuestion.value = nameQuestion
        it.either(::handleFailure, ::handleAnswersPhoto) }

    private fun handleAnswersPhoto(answers : List<Answer>) {
        _answersPhoto.value = answers
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

    fun setReadyAnswers(idQuestion: String,nameQuestion: String,idAnswer:String,nameAnswer: String,img:String) {
        setReadyAnswers(SetReadyAnswers.Params(0,idQuestion,nameQuestion,idAnswer,nameAnswer,img)) {
            it.either(::handleFailure, ::handleReadyAnswers)
        }
    }

    private fun handleReadyAnswers(success: Boolean) {
        this._successReadyAnswers.value = success
    }

    fun updateAnswers(idAnswer: String,data: String) { updateAnswers(UpdateAnswers.Params(idAnswer,data)) {
         it.either(::handleFailure, ::handleUpdateAnswers)
        }
    }
    private fun handleUpdateAnswers(success: Boolean) {
        this._successUpdateAnswers.value = success
    }

    fun updateState(state:Boolean,verify:Boolean) { updateState(UpdateState.Params(state,verify)) {
        it.either(::handleFailure, ::handleUpdateState)
    }
    }
    private fun handleUpdateState(success: Boolean) {
        this._successUpdateState.value = success
    }

    fun getStateReport(idReport: String,type:Int) { getStateReport(GetStateReport.Params(idReport)) {
        _type.value=type
        it.either(::handleFailure, ::handleGetStateReport)
        }
    }
    private fun handleGetStateReport(type: String) {
        this._stateReport.value = type
    }

    fun updateStateReport(idReport:String,state:String,type:String) { updateStateReport(UpdateStateReport.Params(idReport,state,type)) {
        it.either(::handleFailure, ::handleUpdateStateReport)
        }
    }
    private fun handleUpdateStateReport(success: Boolean) {
        this._updateReportState.value = success
    }

    fun updateReportPv(idReport:String,state:String,type: String,time:String,latitude: String,longitude: String) { updateReportPv(UpdateReportPv.Params(idReport,state,type,time,latitude,longitude)) {
        it.either(::handleFailure, ::handleSetReportPv)
        }
    }
    private fun handleSetReportPv(success: Boolean) {
        this._successSetReportPv.value = success
    }

    fun setAnswerPv(idAnswer: String,idQuestion: String,nameAnswer: String,img: String) { setAnswerPv(SetAnswerPv.Params(idAnswer,idQuestion,nameAnswer,img)) {
        it.either(::handleFailure, ::handleSetAnswerPv)
    }
    }
    private fun handleSetAnswerPv(success: Boolean) {
        this._successSetAnswerPv.value = success
    }

    fun syncStandardReports(idReport: String,latitude:String,longitude:String) = syncStandardReports(SyncStandardReports.Params(idReport,latitude,longitude)) { it.either(::handleFailure, ::handleSyncStandardReport) }

    private fun handleSyncStandardReport(success:Boolean) {
        this._successSyncReportStandard.value = success
    }
}