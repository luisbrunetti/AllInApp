package com.tawa.allinapp.features.reports.picture

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tawa.allinapp.core.interactor.UseCase
import com.tawa.allinapp.core.platform.BaseViewModel
import com.tawa.allinapp.features.init.usecase.SyncPhotoReports
import com.tawa.allinapp.features.reports.picture.usecase.*
import com.tawa.allinapp.models.PhotoReport
import javax.inject.Inject

class PictureViewModel
@Inject constructor(
    private val setPhotoReport: SetPhotoReport,
    private val getLocalPhotoReport: GetLocalPhotoReport,
    private val syncPhotoReports: SyncPhotoReports,
    private val deletePhotoReports: DeletePhotoReports,
    private val getTypePicture: GetTypePicture,
    private val syncOnePicture: SyncOnePicture
):BaseViewModel(){
    private val _successReport = MutableLiveData(false)
    val successReport: LiveData<Boolean>
        get()= _successReport

    private val _report = MutableLiveData<PhotoReport>()
    val report: LiveData<PhotoReport>
        get()= _report

    private val _errorMessage = MutableLiveData("")
    val errorMessage = _errorMessage

    private val _successSyncPhotoReports = MutableLiveData(false)
    val successSyncPhotoReports: LiveData<Boolean>
        get() = _successSyncPhotoReports

    private val _successDeletePhotoReports = MutableLiveData(false)
    val successDeletePhotoReports: LiveData<Boolean>
        get() = _successDeletePhotoReports

    private val _successType = MutableLiveData("")
    val successType: LiveData<String>
        get()= _successType

    private val _successSyncOne = MutableLiveData<Boolean>(false)
    val successSyncOne: LiveData<Boolean>
        get()= _successSyncOne


    fun setError(error:String){
        _errorMessage.value = error
    }

    fun saveReport(report: PhotoReport?,state:String,type:String) {
        setPhotoReport(SetPhotoReport.Params(report, state,type)) {
            it.either(::handleFailure, ::handleReports)
        }
    }

    fun getPhotoReport() = getLocalPhotoReport(UseCase.None()) {
        it.either(::handleFailure, ::handlePhotoReport) }

    fun syncPhotoReport() = syncPhotoReports(SyncPhotoReports.Params("0","0")) { it.either(::handleFailure, ::handleSyncPhotoReport) }

    fun deletePhotoReports() = deletePhotoReports(UseCase.None()) { it.either(::handleFailure, ::handleDeletePhotoReport) }

    private fun handlePhotoReport(reports: PhotoReport) {
        _report.value = reports
    }

    private fun handleReports(reports: Boolean) {
        _successReport.value = reports
    }

    private fun handleSyncPhotoReport(success:Boolean) {
        this._successSyncPhotoReports.value = success
    }

    private fun handleDeletePhotoReport(success:Boolean) {
        this._successDeletePhotoReports.value = success
    }

    fun getTypePicture() = getTypePicture(UseCase.None()) {
        it.either(::handleFailure, ::handleTypeReport) }

    private fun handleTypeReport(type:String) {
        _successType.value = type
    }

    fun syncOnePicture() = syncOnePicture(UseCase.None()) {
        it.either(::handleFailure, ::handleSyncOnePicture) }

    private fun handleSyncOnePicture(success: Boolean) {
        _successSyncOne.value = success
    }
}