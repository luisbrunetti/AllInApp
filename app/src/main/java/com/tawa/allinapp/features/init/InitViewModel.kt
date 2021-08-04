package com.tawa.allinapp.features.init

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tawa.allinapp.core.interactor.UseCase
import com.tawa.allinapp.core.platform.BaseViewModel
import com.tawa.allinapp.features.init.usecase.*
import com.tawa.allinapp.features.reports.GetReportsRemote
import com.tawa.allinapp.features.reports.sku.SyncSku
import com.tawa.allinapp.models.Company
import com.tawa.allinapp.models.Schedule
import java.sql.Timestamp
import java.text.SimpleDateFormat
import javax.inject.Inject

class InitViewModel
@Inject constructor(
    private val getCheckMode: GetCheckMode,
    private val getStateCheck: GetStateCheck,
    private val getCompanies: GetCompanies,
    private val getIdCompany: GetIdCompany,
    private val getReportsRemote: GetReportsRemote,
    private val getQuestionsRemote: GetQuestionsRemote,
    private val getPV: GetPV,
    private val getDescPV: GetDescPV,
    private val getIdPV: GetIdPV,
    private val setIdCompany: SetIdCompany,
    private val setCheckIn: SetCheckIn,
    private val setIdPv: SetIdPv,
    private val getIdUser: GetIdUser,
    private val getUserName: GetUserName,
    private val syncCheck: SyncCheck,
    private val syncPhotoReports: SyncPhotoReports,
    private val updateStatus: UpdateStatus,
    private  val getReportsSku: GetReportsSku,
    private val syncStandardReports: SyncStandardReports,
    private  val syncSku: SyncSku,
    private val syncAudio: SyncAudio,
    private val getAudioRemote: GetAudioRemote

) : BaseViewModel()  {
    private  val formatter: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    var timestamp: Timestamp = Timestamp(System.currentTimeMillis())
    private val _startHome = MutableLiveData(true)
    val startHome: LiveData<Boolean>
        get() = _startHome

    private val _startCheckIn = MutableLiveData(false)
    val startCheckIn: LiveData<Boolean>
        get() = _startCheckIn

    private val _successSyncPhotoReports = MutableLiveData(false)
    val successSyncPhotoReports: LiveData<Boolean>
        get() = _successSyncPhotoReports

    private val _successSyncChecks = MutableLiveData(false)
    val successSyncChecks: LiveData<Boolean>
        get() = _successSyncChecks

    private val _startSetCheckIn = MutableLiveData(false)
    val startSetCheckIn: LiveData<Boolean>
        get() = _startSetCheckIn

    private val _successCheckIn= MutableLiveData(false)
    val successCheckIn: LiveData<Boolean>
        get() = _successCheckIn

    private val _stateCheck= MutableLiveData(false)
    val stateCheck: LiveData<Boolean>
        get() = _stateCheck

    private val _successCheckOut= MutableLiveData(false)
    val successCheckOut: LiveData<Boolean>
        get() = _successCheckOut

    private val _companies = MutableLiveData<List<Company>>()
    val companies: LiveData<List<Company>>
        get()= _companies

    private val _idPv = MutableLiveData<Boolean>(false)
    val idPv: LiveData<Boolean>
        get()= _idPv

    private val _setIdCompanySuccess = MutableLiveData<Boolean>(false)
    val setIdCompanySuccess: LiveData<Boolean>
        get()= _setIdCompanySuccess

    private val _successGetCompanyId= MutableLiveData<String>("")
    val successGetCompanyId: LiveData<String>
        get()= _successGetCompanyId

    private val _getCompany= MutableLiveData<String>("")
    val getCompany: LiveData<String>
        get()= _getCompany

    private val _idUser= MutableLiveData<String>("")
    val idUser: LiveData<String>
        get()= _idUser

    private val _userName= MutableLiveData<String>("")
    val userName: LiveData<String>
        get()= _userName

    private val _positionCompany= MutableLiveData<Int>(-1)
    val positionCompany: LiveData<Int>
        get()= _positionCompany

    private val _positionPv = MutableLiveData<Int>(-1)
    val positionPv: LiveData<Int>
        get()= _positionPv

    private val _schedule = MutableLiveData<List<Schedule>>()
    val schedule: LiveData<List<Schedule>>
        get()= _schedule

    private val _pvDesc = MutableLiveData<String>()
    val pvDesc: LiveData<String>
        get()= _pvDesc

    private val _pvId = MutableLiveData<String>()
    val pvId: LiveData<String>
        get()= _pvId

    private val _dayState = MutableLiveData<Boolean>(false)
    val dayState: LiveData<Boolean>
        get()= _dayState

    private val _checkInMode = MutableLiveData(true)
    val checkInMode = _checkInMode

    private val _successGetReports = MutableLiveData(false)
    val successGetReports: LiveData<Boolean>
        get() = _successGetReports

    private val _successGetQuestions = MutableLiveData(false)
    val successGetQuestions: LiveData<Boolean>
        get() = _successGetQuestions

    private val _successUpdate = MutableLiveData(false)
    val successUpdate: LiveData<Boolean>
        get() = _successUpdate

    private val _successReportsSku = MutableLiveData(false)
    val successReportsSku: LiveData<Boolean>
        get() = _successReportsSku

    private val _successSyncReportStandard = MutableLiveData(false)
    val successSyncReportStandard : LiveData<Boolean>
        get() = _successSyncReportStandard

    private val _successSyncSku = MutableLiveData(false)
    val successSyncSku : LiveData<Boolean>
        get() = _successSyncSku

    private val _successSyncAudio = MutableLiveData(false)
    val successSyncAudio : LiveData<Boolean>
        get() = _successSyncAudio

    private val _successGetAudioRemote = MutableLiveData(false)
    val successGetAudioRemote: LiveData<Boolean>
        get() = _successGetAudioRemote

    init {
        startHome()
        startCheckIn()
        getDay()
    }

    fun setCheckIn(idUser:String,pv:String,lat:String,lon:String) {
        _startSetCheckIn.value = true
        setCheckIn(SetCheckIn.Params(0,pv,idUser,formatter.format(timestamp),lat,lon,"CHECKIN")) {
            it.either(::handleFailure, ::handleCheckIn)
        }
    }
    fun setCheckOut(idUser:String,pv:String,lat:String,long:String) {
        _startSetCheckIn.value = false
        setCheckIn(SetCheckIn.Params(0,pv,idUser,formatter.format(timestamp),lat,long,"CHECKOUT")) {
            it.either(::handleFailure, ::handleCheckOut)
        }
    }

    private fun getDay(){ _dayState.value = true }

    fun selectPositionCompany(position : Int) { _positionCompany.value = position }

    fun selectPositionPv(position : Int) { _positionPv.value = position }

    private fun startHome(){ _startHome.value = true }

    private fun startCheckIn(){ _startCheckIn.value = true }

    fun getIdCompany() = getIdCompany(UseCase.None()) { it.either(::handleFailure, ::handleGetIdCompany) }

    fun getIdUser() = getIdUser(UseCase.None()) { it.either(::handleFailure, ::handleGetIdUser) }

    fun getUserName() = getUserName(UseCase.None()) { it.either(::handleFailure, ::handleGetUserName) }

    fun setIdCompany(idCompany:String) = setIdCompany(SetIdCompany.Params(idCompany)) { it.either(::handleFailure, ::handleSetIdCompany) }

    fun setPv(schedule:String,pv:String,descPv:String) = setIdPv(SetIdPv.Params(schedule,pv,descPv)) { it.either(::handleFailure, ::handleSetIdPv) }

    fun getDescPV() = getDescPV(UseCase.None()) { it.either(::handleFailure, ::handlePVDesc) }

    fun getIdPV() = getIdPV(UseCase.None()) { it.either(::handleFailure, ::handlePVId) }

    fun getCompanies() = getCompanies(UseCase.None()) { it.either(::handleFailure, ::handleCompanyList) }

    fun getReportsRemote(company: String) = getReportsRemote(GetReportsRemote.Params(company)) {
        it.either(::handleFailure, ::handleReportsRemote)
    }
    private fun handleReportsRemote(success: Boolean) {
        this._successGetReports.value = success
    }

    fun getQuestionsRemote() = getQuestionsRemote(UseCase.None()) {
        it.either(::handleFailure, ::handleQuestionsRemote)
    }
    private fun handleQuestionsRemote(success: Boolean) {
        this._successGetQuestions.value = success
    }

    fun getPv(company:String) = getPV(GetPV.Params(company)) { it.either(::handleFailure, ::handlePvList) }

    fun getCheckMode() = getCheckMode(UseCase.None()) { it.either(::handleFailure, ::handleCheckMode) }

    fun getStateCheck(idPv:String) = getStateCheck(GetStateCheck.Params(idPv)) { it.either(::handleFailure, ::handleGetStateCheck) }

    fun updateStatus(latitude:String,longitude:String,battery:String) = updateStatus(UpdateStatus.Params(latitude,longitude,battery)) { it.either(::handleFailure, ::handleUpdateStatus) }

    fun getReportsSku() = getReportsSku(UseCase.None()) { it.either(::handleFailure, ::handleReportsSku) }

    fun syncCheck() = syncCheck(UseCase.None()) { it.either(::handleFailure, ::handleSyncCheck) }

    fun syncPhotoReport() = syncPhotoReports(UseCase.None()) { it.either(::handleFailure, ::handleSyncPhotoReport) }

    private fun handleSyncPhotoReport(success:Boolean) {
        this._successSyncPhotoReports.value = success
    }

    private fun handleSyncCheck(success:Boolean) {
        this._successSyncChecks.value = success
    }

    private fun handlePVDesc(checkIn:String) {
        this._pvDesc.value = checkIn
    }
    private fun handlePVId(checkIn:String) {
        this._pvId.value = checkIn
    }
    private fun handleCheckIn(success: Boolean) {
        this._successCheckIn.value = success
    }
    private fun handleCheckOut(success: Boolean) {
        this._successCheckOut.value = success
    }
    private fun handleCheckMode(checkIn:Boolean) {
        this._checkInMode.value = checkIn
    }
    private fun handleGetStateCheck(stateCheck:Boolean) {
        this._stateCheck.value = stateCheck
    }
    private fun handleCompanyList(company: List<Company>) {
        this._companies.value = company.map { Company(it.id,it.ruc,it.name,it.description) }
    }
    private fun handlePvList(schedule: List<Schedule>) {
        this._schedule.value = schedule.map { Schedule(it.id,it.pv,it.description,it.zone,it.codGeo,it.idCompany, it.lat,it.long) }
    }
    private fun handleSetIdCompany(success: Boolean) {
        _setIdCompanySuccess.value = success
    }
    private fun handleSetIdPv(success: Boolean) {
        _idPv.value = success
    }
    private fun handleGetIdCompany(idCompany: String) {
        _successGetCompanyId.value = idCompany
    }
    private fun handleGetIdUser(idCompany: String) {
        _idUser.value = idCompany
    }
    private fun handleGetUserName(user: String) {
        _userName.value = user
    }

    private fun handleUpdateStatus(success: Boolean) {
        this._successUpdate.value = success
    }

    private fun handleReportsSku(success: Boolean) {
        this._successReportsSku.value = success
    }

    fun syncStandardReports() = syncStandardReports(UseCase.None()) { it.either(::handleFailure, ::handleSyncStandardReport) }

    private fun handleSyncStandardReport(success:Boolean) {
        this._successSyncReportStandard.value = success
    }

    fun syncSku() = syncSku(UseCase.None()) { it.either(::handleFailure, ::handleSyncSku) }

    private fun handleSyncSku(success:Boolean) {
        this._successSyncSku.value = success
    }

    fun getAudioRemote() = getAudioRemote(UseCase.None()) {
        it.either(::handleFailure, ::handleGetAudioRemote) }

    private fun handleGetAudioRemote(success:Boolean) {
        this._successGetAudioRemote.value = success
    }

    fun syncAudio() = syncAudio(UseCase.None()) { it.either(::handleFailure, ::handleSyncAudio) }

    private fun handleSyncAudio(success:Boolean) {
        this._successSyncAudio.value = success
    }

}