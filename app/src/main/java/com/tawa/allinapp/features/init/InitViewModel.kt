package com.tawa.allinapp.features.init

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tawa.allinapp.core.interactor.UseCase
import com.tawa.allinapp.core.platform.BaseViewModel
import com.tawa.allinapp.models.Company
import com.tawa.allinapp.models.PV
import com.tawa.allinapp.features.init.usecase.GetCompanies
import com.tawa.allinapp.features.init.usecase.GetPV
import javax.inject.Inject

class InitViewModel
@Inject constructor(
    private val getCompanies: GetCompanies,
    private val getPV: GetPV,
) : BaseViewModel() {

    private val _startHome = MutableLiveData(true)
    val startHome: LiveData<Boolean>
        get() = _startHome

    private val _startCheckIn = MutableLiveData(false)
    val startCheckIn: LiveData<Boolean>
        get() = _startCheckIn

    private val _companies = MutableLiveData<List<Company>>()
    val companies: LiveData<List<Company>>
        get()= _companies

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

    private fun getDay(){
        _dayState.value = true
    }

    private fun startHome(){
       _startHome.value = true
    }

    private fun startCheckIn(){
        _startCheckIn.value = true
    }

    fun getCompanies() = getCompanies(UseCase.None()) { it.either(::handleFailure, ::handleCompanieList) }
    fun getPv() = getPV(UseCase.None()) { it.either(::handleFailure, ::handlePvList) }

    private fun handleCompanieList(company: List<Company>) {
        this._companies.value = company.map { Company(it.name,it.description,it.code,it.ruc,it.description) }
    }

    private fun handlePvList(pv: List<PV>) {
        this._pv.value = pv.map { PV(it.id,it.description,it.zone,it.codGeo,it.idCompany, it.lat,it.long) }
    }

}