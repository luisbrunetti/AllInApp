package com.tawa.allinapp.features.reports.userstatus
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tawa.allinapp.core.interactor.UseCase
import com.tawa.allinapp.core.platform.BaseViewModel
import com.tawa.allinapp.models.ReportStatus
import javax.inject.Inject

class UserStatusViewModel
@Inject constructor(
    private val getReportStatus: GetReportStatus
) : BaseViewModel() {

    private val _text = MutableLiveData("USER STATUS")
    val text: LiveData<String>
        get()= _text

    private val _reportStatus = MutableLiveData<List<ReportStatus>>()
    val reportStatus: LiveData<List<ReportStatus>>
        get()= _reportStatus

    private val _order = MutableLiveData(0)
    val order: LiveData<Int>
        get()= _order

    init {
        getReportStatus()
    }

    fun getReportStatus() = getReportStatus(UseCase.None()) { it.either(::handleFailure, ::handleReportStatus) }

    private fun handleReportStatus(reportStatus: List<ReportStatus>) {
        _reportStatus.value = reportStatus
    }

}