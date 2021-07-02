package com.tawa.allinapp.features.init

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tawa.allinapp.core.interactor.UseCase
import com.tawa.allinapp.core.platform.BaseViewModel
import com.tawa.allinapp.features.init.usecase.*
import com.tawa.allinapp.models.Company
import com.tawa.allinapp.models.PV
import java.sql.Timestamp
import java.text.SimpleDateFormat
import javax.inject.Inject

class InitViewModel
@Inject constructor(
    private val getCheckMode: GetCheckMode,
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
) : BaseViewModel()  {
    private  val formatter: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    var timestamp: Timestamp = Timestamp(System.currentTimeMillis())

    private val _startHome = MutableLiveData(true)
    val startHome: LiveData<Boolean>
        get() = _startHome

    private val _startCheckIn = MutableLiveData(false)
    val startCheckIn: LiveData<Boolean>
        get() = _startCheckIn

    private val _startSetCheckIn = MutableLiveData(false)
    val startSetCheckIn: LiveData<Boolean>
        get() = _startSetCheckIn

    private val _successCheckIn= MutableLiveData(false)
    val successCheckIn: LiveData<Boolean>
        get() = _successCheckIn

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

    private val _getIdCompanyPv= MutableLiveData<String>("")
    val getIdCompanyPv: LiveData<String>
        get()= _getIdCompanyPv

    private val _getCompany= MutableLiveData<String>("")
    val getCompany: LiveData<String>
        get()= _getCompany

    private val _idUser= MutableLiveData<String>("")
    val idUser: LiveData<String>
        get()= _idUser

    private val _positionCompany= MutableLiveData<Int>(-1)
    val positionCompany: LiveData<Int>
        get()= _positionCompany

    private val _positionPv = MutableLiveData<Int>(-1)
    val positionPv: LiveData<Int>
        get()= _positionPv

    private val _pv = MutableLiveData<List<PV>>()
    val pv: LiveData<List<PV>>
        get()= _pv

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

    init {
        startHome()
        startCheckIn()
        getDay()
    }

    fun setCheckIn(idUser:String,idPV:String,lat:String,lon:String) {
        _startSetCheckIn.value = true
        setCheckIn(SetCheckIn.Params(0,idUser,idPV,formatter.format(timestamp),lat,lon,"CHECKIN")) {
            it.either(::handleFailure, ::handleCheckIn)
        }
    }
    fun setCheckOut(idUser:String,idPV:String,lat:String,long:String) {
        _startSetCheckIn.value = false
        setCheckIn(SetCheckIn.Params(0,idUser,idPV,formatter.format(timestamp),lat,long,"CHECKOUT")) {
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

    fun setIdCompany(idCompany:String) = setIdCompany(SetIdCompany.Params(idCompany)) { it.either(::handleFailure, ::handleSetIdCompany) }

    fun setPv(idPv:String,descPv:String) = setIdPv(SetIdPv.Params(idPv,descPv)) { it.either(::handleFailure, ::handleSetIdPv) }

    fun getDescPV() = getDescPV(UseCase.None()) { it.either(::handleFailure, ::handlePVDesc) }

    fun getIdPV() = getIdPV(UseCase.None()) { it.either(::handleFailure, ::handlePVId) }

    fun getCompanies() = getCompanies(UseCase.None()) { it.either(::handleFailure, ::handleCompanyList) }

    fun getReportsRemote() = getReportsRemote(com.tawa.allinapp.core.interactor.UseCase.None()) {
        it.either(::handleFailure, ::handleReportsRemote)
    }
    private fun handleReportsRemote(success: Boolean) {
        this._successGetReports.value = success
    }

    fun getQuestionsRemote() = getQuestionsRemote(com.tawa.allinapp.core.interactor.UseCase.None()) {
        it.either(::handleFailure, ::handleQuestionsRemote)
    }
    private fun handleQuestionsRemote(success: Boolean) {
        this._successGetQuestions.value = success
    }

    fun getPv(company:String) = getPV(GetPV.Params(company)) { it.either(::handleFailure, ::handlePvList) }

    fun getCheckMode() = getCheckMode(UseCase.None()) { it.either(::handleFailure, ::handleCheckMode) }

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
    private fun handleCompanyList(company: List<Company>) {
        this._companies.value = company.map { Company(it.id,it.code,it.ruc,it.name,it.description) }
    }
    private fun handlePvList(pv: List<PV>) {
        this._pv.value = pv.map { PV(it.id,it.description,it.zone,it.codGeo,it.idCompany, it.lat,it.long) }
    }
    private fun handleSetIdCompany(success: Boolean) {
        _setIdCompanySuccess.value = success
    }
    private fun handleSetIdPv(success: Boolean) {
        _idPv.value = success
    }
    private fun handleGetIdCompany(idCompany: String) {
        _getIdCompanyPv.value = idCompany
        _getCompany.value = idCompany
    }
    private fun handleGetIdUser(idCompany: String) {
        _idUser.value = idCompany
    }


}