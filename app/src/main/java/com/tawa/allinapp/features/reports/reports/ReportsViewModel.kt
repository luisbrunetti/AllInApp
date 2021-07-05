package com.tawa.allinapp.features.reports.reports

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tawa.allinapp.core.interactor.UseCase
import com.tawa.allinapp.core.platform.BaseViewModel
import com.tawa.allinapp.models.Question
import com.tawa.allinapp.models.Report
import javax.inject.Inject

class ReportsViewModel
@Inject constructor (
    private val getReports: GetReports
):BaseViewModel() {

    private val _reports = MutableLiveData<List<Report>>()
    val reports: LiveData<List<Report>>
        get()= _reports

    fun getReports() = getReports(UseCase.None()) { it.either(::handleFailure, ::handleReports) }

    private fun handleReports(reports: List<Report>) {
        _reports.value = reports
    }

}