package com.tawa.allinapp.features.reports.picture

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tawa.allinapp.core.platform.BaseViewModel
import com.tawa.allinapp.models.PhotoReport
import com.tawa.allinapp.models.Report
import javax.inject.Inject

class PictureViewModel
@Inject constructor(
    private val setPhotoReport:SetPhotoReport
):BaseViewModel(){
    private val _successReport = MutableLiveData(false)
    val successReport: LiveData<Boolean>
        get()= _successReport

    private val _errorMessage = MutableLiveData("")
    val errorMessage = _errorMessage

    fun setError(error:String){
        _errorMessage.value = error
    }

    fun saveReport(report: PhotoReport) = setPhotoReport(SetPhotoReport.Params(report)) { it.either(::handleFailure, ::handleReports) }

    private fun handleReports(reports: Boolean) {
        _successReport.value = reports
    }
}