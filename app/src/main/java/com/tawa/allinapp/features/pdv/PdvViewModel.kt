package com.tawa.allinapp.features.pdv

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tawa.allinapp.core.interactor.UseCase
import com.tawa.allinapp.core.platform.BaseViewModel
import com.tawa.allinapp.models.Pdv
import javax.inject.Inject

class PdvViewModel
    @Inject constructor(
        private val getPdv: GetPdv,
        private val updatePdvRemote: UpdatePdvRemote,
        private val updatePdv: UpdatePdv,
        private val updateStatePdv: UpdateStatePdv
    ) : BaseViewModel() {

    private val _text = MutableLiveData<String>("Calendario")
    val text: LiveData<String>
        get()= _text

    private val _successGetPdv = MutableLiveData<Pdv>()
    val successGetPdv: LiveData<Pdv>
        get()= _successGetPdv

    private val _successUpdatePdv = MutableLiveData(false)
    val successUpdatePdv: LiveData<Boolean>
        get()= _successUpdatePdv

    private val _successUpdatePdvLocal = MutableLiveData(false)
    val successUpdatePdvLocal: LiveData<Boolean>
        get()= _successUpdatePdvLocal

    private val _successUpdatePdvState = MutableLiveData(false)
    val successUpdatePdvState: LiveData<Boolean>
        get()= _successUpdatePdvState


    fun getPdv() = getPdv(UseCase.None()) {
        it.either(::handleFailure, ::handleGetPdv) }

    private fun handleGetPdv(pdv:Pdv) {
        _successGetPdv.value = pdv
    }

    fun updatePdvRemote(idPdv: String,nameUser:String,phoneUser:String,ruc:String,latitude:String,longitude:String,image:String) = updatePdvRemote(UpdatePdvRemote.Params(idPdv,nameUser,phoneUser,ruc,latitude,longitude,image)) {
        it.either(::handleFailure, ::handleUpdatePdv) }

    private fun handleUpdatePdv(success:Boolean) {
        _successUpdatePdv.value = success
    }

    fun updatePdv(idPdv: String,nameUser:String,phoneUser:String,ruc:String,latitude:String,longitude:String,image:String,state:String) = updatePdv(UpdatePdv.Params(idPdv,nameUser,phoneUser,ruc,latitude,longitude,image,state)) {
        it.either(::handleFailure, ::handleUpdatePdvLocal) }

    private fun handleUpdatePdvLocal(success:Boolean) {
        _successUpdatePdvLocal.value = success
    }

    fun updatePdv(idPdv: String,state:String) = updateStatePdv(UpdateStatePdv.Params(idPdv,state)) {
        it.either(::handleFailure, ::handleUpdatePdvState) }

    private fun handleUpdatePdvState(success:Boolean) {
        _successUpdatePdvState.value = success
    }

}