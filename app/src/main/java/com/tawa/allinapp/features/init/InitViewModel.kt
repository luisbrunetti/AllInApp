package com.tawa.allinapp.features.init

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tawa.allinapp.core.interactor.UseCase
import com.tawa.allinapp.core.platform.BaseViewModel
import com.tawa.allinapp.data.local.Prefs
import com.tawa.allinapp.features.init.usecase.*
import com.tawa.allinapp.features.reports.GetReportsRemote
import com.tawa.allinapp.models.*
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*
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
    private val getLogoCompany: GetLogoCompany,
    private val getIdPV: GetIdPV,
    private val setIdCompany: SetIdCompany,
    private val setCheckIn: SetCheckIn,
    private val setIdPv: SetIdPv,
    private val setSession: SetSession,
    private val deletePv: DeletePv,
    private val getIdUser: GetIdUser,
    private val getRoleUser: GetRoleUser,
    private val getUserName: GetUserName,
    private val syncCheck: SyncCheck,
    private val syncPhotoReports: SyncPhotoReports,
    private val updateStatus: UpdateStatus,
    private val getReportsSku: GetReportsSku,
    private val syncStandardReportsMassive: SyncStandardReportsMassive,
    private val syncSkuMassive: SyncSkuMassive,
    private val syncAudio: SyncAudio,
    private val listReports: ListReports,
    private val sendPassword: SendPassword,
    private val getPdvRemote: GetPdvRemote,
    private val sendCheck: SendCheck,
    private val pref:Prefs,
    private val updateCountNotify: UpdateCountNotify,
    private val getCountNotify: GetCountNotify,
    private val clearNotify: ClearNotify,
    private val getNotify: GetNotify,
    private val getLanguageByXml: GetLanguageByXml,
    private val getLanguage: GetLanguage
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

    private val _successGetCompanyId= MutableLiveData<String>()
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

    private val _logoCompany= MutableLiveData<String>("")
    val logoCompany: LiveData<String>
        get()= _logoCompany

    private val _positionCompany= MutableLiveData<Int>(-1)
    val positionCompany: LiveData<Int>
        get()= _positionCompany

    private val _positionPv = MutableLiveData<Int>(-1)
    val positionPv: LiveData<Int>
        get()= _positionPv

    private val _countNotify = MutableLiveData<Int>(0)
    val countNotify: LiveData<Int>
        get()= _countNotify

    private val _clearCountNotify = MutableLiveData<Boolean>(false)
    val clearCountNotify: LiveData<Boolean>
        get()= _clearCountNotify

    private val _updateNotify = MutableLiveData<Boolean>(false)
    val updateNotify: LiveData<Boolean>
        get()= _updateNotify

    private val _successGetNotify = MutableLiveData<List<Notify>>()
    val successGetNotify: LiveData<List<Notify>>
        get()= _successGetNotify

    private val _schedule = MutableLiveData<List<Schedule>>()
    val schedule: LiveData<List<Schedule>>
        get()= _schedule

    private val _pvDesc = MutableLiveData<String>()
    val pvDesc: LiveData<String>
        get()= _pvDesc

    private val _descPV = MutableLiveData<String>()
    val descPV: LiveData<String>
        get()= _descPV

    private val _pvId = MutableLiveData<String>()
    val pvId: LiveData<String>
        get()= _pvId

    private val _idPV = MutableLiveData<String>()
    val idPV: LiveData<String>
        get()= _idPV

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

    private val _successListReport = MutableLiveData<List<Report>>()
    val successListReport: LiveData<List<Report>>
        get() = _successListReport

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

    private val _successGetRole = MutableLiveData("")
    val successGetRole: LiveData<String>
        get() = _successGetRole

    private val _successSendPassword = MutableLiveData<Boolean>(false)
    val successSendPassword: LiveData<Boolean>
        get() = _successSendPassword

    private val _successGetPdvRemote = MutableLiveData<Boolean>(false)
    val successGetPdvRemote: LiveData<Boolean>
        get() = _successGetPdvRemote

    private val _successSendCheck = MutableLiveData<String>()
    val successSendCheck: LiveData<String>
        get() = _successSendCheck

    private val _type= MutableLiveData<Int>(-1)
    val type: LiveData<Int>
        get()= _type

    private val _getPvIdf= MutableLiveData<String>()
    val getPvIdf: LiveData<String>
        get()= _getPvIdf

    private val _selector = MutableLiveData<Boolean>()
    val selector:  LiveData<Boolean> get() = _selector

    private val _deletePvId = MutableLiveData<Boolean>()
    val deletePvId : LiveData<Boolean> get() = _deletePvId

    private val _setSession = MutableLiveData<Boolean>()
    val setSessionLiveData : LiveData<Boolean> get() = _setSession

    private val _getLanguageSuccess = MutableLiveData<List<Language>>()
    val getLanguageSuccess : LiveData<List<Language>> get() = _getLanguageSuccess

    private var _getLanguagePref = MutableLiveData<String>()
    var getLanguagePref : LiveData<String> = _getLanguagePref

    init {
        startHome()
        startCheckIn()
        getDay()
    }

    fun setCheckIn(idUser:String,pv:String,lat:String,lon:String) {
        _startSetCheckIn.value = true
        setCheckIn(SetCheckIn.Params(0,pv,idUser,Calendar.getInstance().toInstant().toString(),lat,lon,"CHECKIN","no enviado")) {
            it.either(::handleFailure, ::handleCheckIn)
        }
    }
    fun setCheckOut(idUser:String,pv:String,lat:String,long:String) {
        setCheckIn(SetCheckIn.Params(0,pv,idUser,Calendar.getInstance().toInstant().toString(),lat,long,"CHECKOUT","no enviado")) {
            it.either(::handleFailure, ::handleCheckOut)
        }
    }

    private fun getDay(){ _dayState.value = true }

    fun selectPositionCompany(position : Int) { _positionCompany.value = position }

    fun selectPositionPv(position : Int) { _positionPv.value = position }

    private fun startHome(){ _startHome.value = true }

    private fun startCheckIn(){ _startCheckIn.value = true }

    fun getIdCompany() = getIdCompany(UseCase.None()) { it.either(::handleFailure, ::handleGetIdCompany) }

    fun getIdCompanyPreferences(): String{ return pref.companyId!! }

    fun getIdUser() = getIdUser(UseCase.None()) { it.either(::handleFailure, ::handleGetIdUser) }

    fun getUserName() = getUserName(UseCase.None()) { it.either(::handleFailure, ::handleGetUserName) }

    fun setIdCompany(idCompany:String,image:String) = setIdCompany(SetIdCompany.Params(idCompany,image)) { it.either(::handleFailure, ::handleSetIdCompany) }

    fun deletePvId() = deletePv(UseCase.None()){ it.either(::handleFailure, ::handleDeletePvId) }

    private fun handleDeletePvId(value : Boolean){ this._deletePvId.value = value }

    fun setPv(schedule:String,pv:String,descPv:String) = setIdPv(SetIdPv.Params(schedule,pv,descPv)) { it.either(::handleFailure, ::handleSetIdPv) }

    fun getDescPV() = getDescPV(UseCase.None()) { it.either(::handleFailure, ::handlePVDesc) }

    fun getPVDesc() = getDescPV(UseCase.None()) { it.either(::handleFailure, ::handleDescPV) }

    fun getIdPV() = getIdPV(UseCase.None()) { it.either(::handleFailure, ::handlePVId) }

    fun getCompanies() = getCompanies(UseCase.None()) { it.either(::handleFailure, ::handleCompanyList) }

    fun getReportsRemote(company: String) = getReportsRemote(GetReportsRemote.Params(company)) { it.either(::handleFailure, ::handleReportsRemote) }

    private fun handleReportsRemote(success: Boolean) { this._successGetReports.value = success }

    fun getQuestionsRemote(idReport: String) = getQuestionsRemote(GetQuestionsRemote.Params(idReport)) { it.either(::handleFailure, ::handleQuestionsRemote) }

    private fun handleQuestionsRemote(success: Boolean) { this._successGetQuestions.value = success }

    fun getPv(company:String) = getPV(GetPV.Params(company)) { it.either(::handleFailure, ::handlePvList) }

    fun getCheckMode() = getCheckMode(UseCase.None()) { it.either(::handleFailure, ::handleCheckMode) }

    fun getStateCheck(idPv:String) = getStateCheck(GetStateCheck.Params(idPv)) { it.either(::handleFailure, ::handleGetStateCheck) }

    fun updateStatus(latitude:String,longitude:String,battery:String,type:Int) = updateStatus(UpdateStatus.Params(latitude,longitude,battery)) {
        _type.value  = type
        _successCheckIn.value = false
        _successCheckOut.value = false
        it.either(::handleFailure, ::handleUpdateStatus) }

    fun getReportsSku(company: String) = getReportsSku(GetReportsSku.Params(company)) { it.either(::handleFailure, ::handleReportsSku) }

    fun syncCheck(latitude: String,longitude: String) = syncCheck(SyncCheck.Params(latitude,longitude)) { it.either(::handleFailure, ::handleSyncCheck) }

    fun syncPhotoReportMassive(latitude: String,longitude: String) = syncPhotoReports(SyncPhotoReports.Params(latitude,longitude)) { it.either(::handleFailure, ::handleSyncPhotoReport) }

    private fun handleSyncPhotoReport(success:Boolean) { this._successSyncPhotoReports.value = success }

    private fun handleSyncCheck(success:Boolean) { this._successSyncChecks.value = success }

    private fun handlePVDesc(checkIn:String) { this._pvDesc.value = checkIn }

    private fun handleDescPV(checkIn:String) { this._descPV.value = checkIn }

    private fun handlePVId(checkIn:String) { this._pvId.value = checkIn }

    private fun handleIdPV(checkIn:String) { this._idPV.value = checkIn }

    private fun handleCheckIn(success: Boolean) { this._successCheckIn.value = success }

    private fun handleCheckOut(success: Boolean) { this._successCheckOut.value = success }

    private fun handleCheckMode(checkIn:Boolean) { this._checkInMode.value = checkIn }

    private fun handleGetStateCheck(stateCheck:Boolean) { this._stateCheck.value = stateCheck }

    private fun handleCompanyList(company: List<Company>) { this._companies.value = company.map { Company(it.id,it.ruc,it.name,it.description,it.idUser,it.image) } }

    private fun handlePvList(schedule: List<Schedule>) { this._schedule.value = schedule.map { Schedule(it.id,it.pv,it.description,it.zone,it.codGeo,it.idCompany, it.lat,it.long,it.idUser,it.nameCorp) } }

    private fun handleSetIdCompany(success: Boolean) { _setIdCompanySuccess.value = success }

    private fun handleSetIdPv(success: Boolean) { _idPv.value = success }

    private fun handleGetIdCompany(idCompany: String) { _successGetCompanyId.value = idCompany }

    private fun handleGetIdUser(getIdUser: String) { _idUser.value = getIdUser }

    private fun handleGetUserName(user: String) { _userName.value = user }

    private fun handleUpdateStatus(success: Boolean) { this._successUpdate.value = success }

    private fun handleReportsSku(success: Boolean) { this._successReportsSku.value = success }

    fun syncStandardReportsMassive(latitude: String,longitude: String) = syncStandardReportsMassive(SyncStandardReportsMassive.Params(latitude,longitude)) { it.either(::handleFailure, ::handleSyncStandardReport) }

    private fun handleSyncStandardReport(success:Boolean) { this._successSyncReportStandard.value = success }

    fun syncSkuMassive(latitude: String,longitude: String) = syncSkuMassive(SyncSkuMassive.Params(latitude,longitude)) { it.either(::handleFailure, ::handleSyncSku) }

    private fun handleSyncSku(success:Boolean) { this._successSyncSku.value = success }

    private fun handleGetAudioRemote(success:Boolean) { this._successGetAudioRemote.value = success }

    fun syncAudio() = syncAudio(UseCase.None()) { it.either(::handleFailure, ::handleSyncAudio) }

    private fun handleSyncAudio(success:Boolean) { this._successSyncAudio.value = success }

    fun getRoleUser() = getRoleUser(UseCase.None()) { it.either(::handleFailure, ::handleGetRole) }

    private fun handleGetRole(role:String) { this._successGetRole.value = role }

    fun listReports(idCompany: String) = listReports(ListReports.Params(idCompany)) { it.either(::handleFailure, ::handleListReports) }

    private fun handleListReports(reports:List<Report>) { this._successListReport.value = reports }

    fun sendPassword(password: String) =sendPassword(SendPassword.Params(password)) { it.either(::handleFailure, ::handleSendPassword) }

    private fun handleSendPassword(success: Boolean) { this._successSendPassword.value = success }

    fun getPdvRemote(idCompany: String) =getPdvRemote(GetPdvRemote.Params(idCompany)) { it.either(::handleFailure, ::handleGetPdvRemote) }

    private fun handleGetPdvRemote(success: Boolean) { this._successGetPdvRemote.value = success }

    fun getLogoCompany() =getLogoCompany(UseCase.None()) { it.either(::handleFailure, ::handleGetLogoCompany) }

    private fun handleGetLogoCompany(image  : String) { this._logoCompany.value = image }

    fun updateCountNotify() =updateCountNotify(UseCase.None()) { it.either(::handleFailure, ::handleUpdateCountNotify) }

    private fun handleUpdateCountNotify(success: Boolean) { this._updateNotify.value = success }

    fun getCountNotify() =getCountNotify(UseCase.None()) { it.either(::handleFailure, ::handleGetCountNotify) }

    private fun handleGetCountNotify(count:Int) { this._countNotify.value = count }

    fun clearCountNotify() =clearNotify(UseCase.None()) { it.either(::handleFailure, ::handleClearCountNotify) }

    private fun handleClearCountNotify(success: Boolean) { this._clearCountNotify.value = success }

    fun getNotify() =getNotify(UseCase.None()) { it.either(::handleFailure, ::handleGetNotify) }

    private fun handleGetNotify(notify: List<Notify>) { this._successGetNotify.value = notify }


    fun sendCheck(latitude: String,longitude: String,type:Int) = sendCheck(SendCheck.Params(latitude,longitude,type)) {
        this._successUpdate.value = false
        it.either(::handleFailure, ::handleSendCheck)
    }

    private fun handleSendCheck(success: String) { this._successSendCheck.value = success }

    fun setSession(value : Boolean)= setSession(SetSession.Params(value)){ it.either(::handleFailure, ::handleSetSession) }

    private fun handleSetSession(value:Boolean){ _setSession.value = value }

    fun getPvIdFirstTime() = getIdPV(UseCase.None()) { it.either(::handleFailure, ::getPvId) }

    private fun getPvId(value : String){ _getPvIdf.value = value }

    fun getLanguageByXml(xmlName:String){ getLanguageByXml(GetLanguageByXml.Params(xmlName)){ it.either(::handleFailure,::handleGetLanguage)}}

    private fun handleGetLanguage(list : List<Language>){ this._getLanguageSuccess.value = list }

    fun getLanguage()= getLanguage(UseCase.None()){ it.either(::handleFailure, ::handleSuccessGetLanguage)}

    fun handleSuccessGetLanguage(value: String){
        this._getLanguagePref.value = value
    }

    fun changeCheckState(value:String){ this._successSendCheck.value = value }

    fun changeStatePv(value: String){ this._pvId.value = value }

    fun changeStateSuccessCompanyId(value:String){ this._successGetCompanyId.value = value }

    fun changeStateSuccessCheckIn(value: Boolean){ this._successCheckIn.value = value}

    fun changeSuccessUpdate(value: Boolean) {this._successUpdate.value = value}

    fun changeStateSuccessCheckout(value:Boolean) {this._successCheckOut.value = value}

    fun changeStatePvDesc(value:String) {this._pvDesc.value = value}
}