package com.tawa.allinapp.features.init

import android.os.Build
import android.view.Menu
import android.view.MenuInflater
import android.widget.ArrayAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tawa.allinapp.R
import com.tawa.allinapp.core.interactor.UseCase
import com.tawa.allinapp.core.platform.BaseViewModel
import com.tawa.allinapp.data.local.models.CompanyModel
import com.tawa.allinapp.features.auth.Company
import com.tawa.allinapp.features.auth.PV
import com.tawa.allinapp.features.auth.usecase.DoLogin
import com.tawa.allinapp.features.movies.MovieView
import com.tawa.allinapp.features.movies.models.Movie
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.Month
import javax.inject.Inject

class InitViewModel
@Inject constructor(
    private val getCompanies: GetCompanies,
    private val getPV: GetPV,
) : BaseViewModel() {

    private val _text = MutableLiveData<String>("Inicio")
    val text: LiveData<String>
        get()= _text


    private val _startHome = MutableLiveData(true)
    val startHome: LiveData<Boolean>
        get() = _startHome

    private val _startCheckIn = MutableLiveData(false)
    val startCheckIn: LiveData<Boolean>
        get() = _startCheckIn


    private val _options = MutableLiveData<Boolean>(false)
    val options: LiveData<Boolean>
        get()= _options

    private val _companies = MutableLiveData<List<Company>>()
    val companies: LiveData<List<Company>>
        get()= _companies

    private val _pv = MutableLiveData<List<PV>>()
    val pv: LiveData<List<PV>>
        get()= _pv


    private val _dayWeek = MutableLiveData<String>("")
    val dayWeek: LiveData<String>
        get()= _dayWeek

    private val _dayMonth = MutableLiveData<String>("")
    val dayMonth: LiveData<String>
        get()= _dayMonth

    private val _month = MutableLiveData<String>("")
    val month: LiveData<String>
        get()= _month

    private val _year = MutableLiveData<String>("")
    val year: LiveData<String>
        get()= _year

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

        val current = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDateTime.now()

        } else {
            TODO("VERSION.SDK_INT < O")
        }
        _dayMonth.value =current.dayOfMonth.toString()
        _year.value =current.year.toString()

        when(current.dayOfWeek){

            DayOfWeek.SUNDAY -> _dayWeek.value= "Domingo"
            DayOfWeek.MONDAY -> _dayWeek.value= "Lunes"
            DayOfWeek.TUESDAY -> _dayWeek.value= "Martes"
            DayOfWeek.WEDNESDAY -> _dayWeek.value= "Miercoles"
            DayOfWeek.THURSDAY -> _dayWeek.value= "Jueves"
            DayOfWeek.FRIDAY -> _dayWeek.value= "Viernes"
            DayOfWeek.SATURDAY -> _dayWeek.value= "Sabado"

        }

        when (current.month)
        {
            Month.JANUARY -> _month.value = "Enero"
            Month.FEBRUARY -> _month.value = "Febrero"
            Month.MARCH -> _month.value = "Marzo"
            Month.APRIL -> _month.value = "Abril"
            Month.MAY -> _month.value = "Mayo"
            Month.JUNE -> _month.value = "Junio"
            Month.JULY -> _month.value = "Julio"
            Month.AUGUST -> _month.value = "Agosto"
            Month.SEPTEMBER -> _month.value = "Septiembre"
            Month.OCTOBER -> _month.value = "Octubre"
            Month.NOVEMBER -> _month.value = "Noviembre"
            Month.DECEMBER -> _month.value = "Diciembre"
        }


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
        this._companies.value = company.map { Company(it.name,it.description,it.code,it.ruc,it.description)}
    }

    private fun handlePvList(pv: List<PV>) {
        this._pv.value = pv.map { PV(it.id,it.description,it.zone,it.codGeo,it.idCompany)}
    }








}