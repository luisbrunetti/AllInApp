package com.tawa.allinapp.features.reports

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tawa.allinapp.core.interactor.UseCase
import com.tawa.allinapp.core.platform.BaseViewModel
import com.tawa.allinapp.data.local.Prefs
import com.tawa.allinapp.features.init.usecase.GetDescPV
import com.tawa.allinapp.features.reports.picture.usecase.GetStatePicture
import com.tawa.allinapp.models.Report
import javax.inject.Inject

class ReportsViewModel
@Inject constructor (
    private val getReports: GetReports,
    private val getDescPV: GetDescPV,
    private val getUserType: GetUserType,
    private val getStateSku: GetStateSku,
    private val getStatePicture: GetStatePicture,
    private val getCountSku: GetCountSku,
    private val prefs: Prefs
):BaseViewModel() {

    private val _reports = MutableLiveData<List<Report>>()
    val reports: LiveData<List<Report>>
        get()= _reports

    private val _pvName = MutableLiveData("")
    val pvName:LiveData<String>
        get() = _pvName

    private val _userType = MutableLiveData("")
    val userType:LiveData<String>
        get() = _userType

    private val _successStateSku = MutableLiveData("")
    val successStateSku:LiveData<String>
        get() = _successStateSku

    private val _successStatePicture = MutableLiveData("")
    val successStatePicture:LiveData<String>
        get() = _successStatePicture

    private val _countSku = MutableLiveData(0)
    val countSku:LiveData<Int>
        get() = _countSku

    private val _getUserId = MutableLiveData<String>("")
    val getUserId : LiveData<String> get() = _getUserId
    init {
        getUserType()
    }

    fun getReports() = getReports(UseCase.None()) { it.either(::handleFailure, ::handleReports) }

    fun getPVName() = getDescPV(UseCase.None()) { it.either(::handleFailure, ::handleReportName) }

    private fun getUserType() = getUserType(UseCase.None()) { it.either(::handleFailure, ::handleGetUserType) }

    private fun handleReports(reports: List<Report>) {
        _reports.value = reports
    }

    private fun handleReportName(reports: String) {
        _pvName.value = reports
    }

    private fun handleGetUserType(user :String) {
        _userType.value = user
    }

    fun getStateSku(idPv:String) = getStateSku(GetStateSku.Params(idPv)) { it.either(::handleFailure, ::handleSuccessSku) }

    fun getStatePicture() = getStatePicture(UseCase.None()) { it.either(::handleFailure, ::handleSuccessPicture) }

    private fun handleSuccessSku(state:String) {
        _successStateSku.value = state
    }

    private fun handleSuccessPicture(state:String?) {
        _successStatePicture.value = state
    }

    fun getCountSku() = getCountSku(UseCase.None()) { it.either(::handleFailure, ::handleCountSku) }

    private fun handleCountSku(count: Int) {
        _countSku.value = count
    }

    fun getUserId() = prefs.idUser
}