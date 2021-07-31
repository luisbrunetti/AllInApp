package com.tawa.allinapp.features.reports.sku
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tawa.allinapp.core.interactor.UseCase
import com.tawa.allinapp.core.platform.BaseViewModel
import com.tawa.allinapp.data.local.models.SkuDetailModel
import com.tawa.allinapp.features.init.usecase.GetIdCompany
import com.tawa.allinapp.features.reports.userstatus.GetReportStatus
import com.tawa.allinapp.models.*
import javax.inject.Inject

class SkuViewModel
@Inject constructor(
    private val getSkuDetail: GetSkuDetail,
    private val setSkuObservation: SetSkuObservation,
    private val getSkuObservation: GetSkuObservation,
    private val addSku: AddSku,
    private val getSku: GetSku,
    private val updateSkuDetail: UpdateSkuDetail
) : BaseViewModel() {

    private val _successSku = MutableLiveData<List<SkuDetail>>()
    val successSku: LiveData<List<SkuDetail>>
        get()= _successSku

    private val _successGetSku = MutableLiveData<List<Sku>>()
    val successGetSku: LiveData<List<Sku>>
        get()= _successGetSku

    private val _successSetSkuObservation = MutableLiveData<Boolean>(false)
    val successSetSkuObservation: LiveData<Boolean>
        get()= _successSetSkuObservation

    private val _successAddSku = MutableLiveData<Boolean>(false)
    val successAddSku: LiveData<Boolean>
        get()= _successAddSku

    private val _successUpdateSkuDetail = MutableLiveData<Boolean>(false)
    val successUpdateSkuDetail: LiveData<Boolean>
        get()= _successUpdateSkuDetail

    private val _successGetSkuObservation = MutableLiveData<List<SkuObservation>>()
    val successGetSkuObservation: LiveData<List<SkuObservation>>
        get()= _successGetSkuObservation

    private val _type = MutableLiveData<Int>(0)
    val type: LiveData<Int>
        get()= _type

    private val _typeSku = MutableLiveData<Int>(0)
    val typeSku: LiveData<Int>
        get()= _typeSku

    private val _typeIni = MutableLiveData<Int>(0)
    val typeIni: LiveData<Int>
        get()= _typeIni

    init {
        //getSkuDetail(1)
    }

    fun getSkuDetail(idSku:String,typeSku:Int) = getSkuDetail(GetSkuDetail.Params(idSku)) {
        _typeSku.value = typeSku
        it.either(::handleFailure, ::handleSkuDetail) }

    private fun handleSkuDetail(skuDetail: List<SkuDetail>) {
        _successSku.value = skuDetail
    }

    fun updateSkuDetail(idSku:String,stock:Boolean,exhibition:Boolean,price:Float) = updateSkuDetail(UpdateSkuDetail.Params(idSku,stock,exhibition,price)) {
        it.either(::handleFailure, ::handleUpdateSkuDetail) }

    private fun handleUpdateSkuDetail(success: Boolean) {
        _successUpdateSkuDetail.value = success
    }

    fun getSku(typeIni:Int) = getSku(UseCase.None()) {
        _typeIni.value=typeIni
        it.either(::handleFailure, ::handleSku) }

    private fun handleSku(sku: List<Sku>) {
        _successGetSku.value = sku
    }

    fun addSku(idReportPv:String,idPv:String,idCompany: String,lines: List<Lines>) = addSku(AddSku.Params(idReportPv,idPv,idCompany,lines)) {
        it.either(::handleFailure, ::handleAddSku) }

    private fun handleAddSku(success: Boolean) {
        _successAddSku.value = success
    }

    fun setSkuObservation(idSkuDetail: String,observation: String) = setSkuObservation(SetSkuObservation.Params(
        SkuObservation(0,idSkuDetail,observation)
    )) {
        it.either(::handleFailure, ::handleSetSkuObservation) }

    private fun handleSetSkuObservation(success : Boolean) {
        _successSetSkuObservation.value = success
    }

    fun getSkuObservation(idSkuDetail: String,type:Int) = getSkuObservation(GetSkuObservation.Params(idSkuDetail)) {
        _type.value= type
        it.either(::handleFailure, ::handleGetSkuObservation) }

    private fun handleGetSkuObservation(skuObservation:List<SkuObservation>) {
        _successGetSkuObservation.value = skuObservation
    }

}