package com.tawa.allinapp.features.reports.picture

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tawa.allinapp.core.interactor.UseCase
import com.tawa.allinapp.core.platform.BaseViewModel
import com.tawa.allinapp.models.PhotoReport
import com.tawa.allinapp.models.Report
import javax.inject.Inject

class PictureViewModel
@Inject constructor(
    private val setPhotoReport:SetPhotoReport,
    private val getLocalPhotoReport: GetLocalPhotoReport
):BaseViewModel(){
    private val _successReport = MutableLiveData(false)
    val successReport: LiveData<Boolean>
        get()= _successReport

    private val _report = MutableLiveData<PhotoReport>()
    val report: LiveData<PhotoReport>
        get()= _report

    private val _errorMessage = MutableLiveData("")
    val errorMessage = _errorMessage

    fun setError(error:String){
        _errorMessage.value = error
    }

    fun saveReport(report: PhotoReport) = setPhotoReport(SetPhotoReport.Params(report)) {
        it.either(::handleFailure, ::handleReports) }

    fun getPhotoReport() = getLocalPhotoReport(UseCase.None()) { it.either(::handleFailure, ::handlePhotoReport) }

    private fun handlePhotoReport(reports: PhotoReport) {
        _report.value = reports
    }

    private fun handleReports(reports: Boolean) {
        _successReport.value = reports
    }
}