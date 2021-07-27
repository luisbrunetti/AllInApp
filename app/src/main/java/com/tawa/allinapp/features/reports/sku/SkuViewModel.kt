package com.tawa.allinapp.features.reports.sku
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tawa.allinapp.core.interactor.UseCase
import com.tawa.allinapp.core.platform.BaseViewModel
import com.tawa.allinapp.features.reports.userstatus.GetReportStatus
import com.tawa.allinapp.models.ReportStatus
import javax.inject.Inject

class SkuViewModel
@Inject constructor(
    private val getSku: GetSku
) : BaseViewModel() {

    private val _successSku = MutableLiveData("")
    val successSku: LiveData<String>
        get()= _successSku

    init {
        getSku()
    }

    private fun getSku() = getSku(UseCase.None()) { it.either(::handleFailure, ::handleSku) }

    private fun handleSku(sku: String) {
        _successSku.value = sku
    }

}