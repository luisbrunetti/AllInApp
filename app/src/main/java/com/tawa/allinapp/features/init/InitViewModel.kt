package com.tawa.allinapp.features.init

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tawa.allinapp.core.interactor.UseCase
import com.tawa.allinapp.core.platform.BaseViewModel
import com.tawa.allinapp.features.init.usecase.GetCompanies
import com.tawa.allinapp.features.init.usecase.GetPV
import com.tawa.allinapp.features.init.usecase.SetCheckIn
import com.tawa.allinapp.features.init.usecase.SetIdCompany
import com.tawa.allinapp.features.splash.GetIdCompany
import com.tawa.allinapp.models.Company
import com.tawa.allinapp.models.PV
import java.sql.Timestamp
import java.text.SimpleDateFormat
import javax.inject.Inject

class InitViewModel
@Inject constructor(
    private val getCompanies: GetCompanies,
    private val getPV: GetPV,
    private val setCheckIn: SetCheckIn,
    private val setIdCompany: SetIdCompany,
    private val getIdCompany: GetIdCompany,

    ) : BaseViewModel() {
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


    private val _companies = MutableLiveData<List<Company>>()
    val companies: LiveData<List<Company>>
        get()= _companies

    private val _setIdCompanySuccess = MutableLiveData<Boolean>(false)
    val setIdCompanySuccess: LiveData<Boolean>
        get()= _setIdCompanySuccess

    private val _getIdCompanyPv= MutableLiveData<String>("")
    val getIdCompanyPv: LiveData<String>
        get()= _getIdCompanyPv

    private val _positionCompany= MutableLiveData<Int>(-1)
    val positionCompany: LiveData<Int>
        get()= _positionCompany

    private val _positionPv = MutableLiveData<Int>(-1)
    val positionPv: LiveData<Int>
        get()= _positionPv

    private val _pv = MutableLiveData<List<PV>>()
    val pv: LiveData<List<PV>>
        get()= _pv

    private val _dayState = MutableLiveData<Boolean>(false)
    val dayState: LiveData<Boolean>
        get()= _dayState

    init {
        startHome()
        startCheckIn()
        getDay()
    }

    fun setCheckIn(idPV:String,lat:String,lon:String) {
        _startSetCheckIn.value = true
        setCheckIn(SetCheckIn.Params(0,"",idPV,formatter.format(timestamp),lat,lon,"CHECKIN")) {
            it.either(::handleFailure, ::handleCheckIn)
        }
    }

    private fun handleCheckIn(success: Boolean) {
        this._successCheckIn.value = success
    }

    private fun getDay(){
        _dayState.value = true
    }

     fun selectPositionCompany(position : Int)
    {
        _positionCompany.value = position
    }

    fun selectPositionPv(position : Int)
    {
        _positionPv.value = position
    }

    private fun startHome(){
       _startHome.value = true
    }

    private fun startCheckIn(){
        _startCheckIn.value = true
    }

    fun getIdCompany() = getIdCompany(com.tawa.allinapp.core.interactor.UseCase.None()) { it.either(::handleFailure, ::handleGetIdCompany)

    }

    fun setIdCompany(idCompany:String) = setIdCompany(SetIdCompany.Params(idCompany)) { it.either(::handleFailure, ::handleSetIdCompany) }


    fun getCompanies() = getCompanies(UseCase.None()) { it.either(::handleFailure, ::handleCompanyList) }

    fun getPv(company:String) = getPV(GetPV.Params(company)) { it.either(::handleFailure, ::handlePvList) }

    private fun handleCompanyList(company: List<Company>) {
        this._companies.value = company.map { Company(it.id,it.code,it.ruc,it.name,it.description) }
    }

    private fun handlePvList(pv: List<PV>) {
        this._pv.value = pv.map { PV(it.id,it.description,it.zone,it.codGeo,it.idCompany, it.lat,it.long) }
    }
    private fun handleSetIdCompany(success: Boolean) {
        _setIdCompanySuccess.value = success
    }
    private fun handleGetIdCompany(idCompany: String) {
        _getIdCompanyPv.value = idCompany
    }


}