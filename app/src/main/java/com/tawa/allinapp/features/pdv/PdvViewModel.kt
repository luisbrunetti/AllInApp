package com.tawa.allinapp.features.pdv

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tawa.allinapp.core.platform.BaseViewModel
import com.tawa.allinapp.models.Pdv
import javax.inject.Inject

class PdvViewModel
    @Inject constructor(
        private val getPdv: GetPdv,
        private val updatePdvRemote: UpdatePdvRemote
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


    fun getPdv(idPdv:String) = getPdv(GetPdv.Params(idPdv)) {
        it.either(::handleFailure, ::handleGetPdv) }

    private fun handleGetPdv(pdv:Pdv) {
        _successGetPdv.value = pdv
    }

    fun updatePdvRemote(idPdv: String,nameUser:String,phoneUser:String,ruc:String,latitude:String,longitude:String,image:String) = updatePdvRemote(UpdatePdvRemote.Params(idPdv,nameUser,phoneUser,ruc,latitude,longitude,image)) {
        it.either(::handleFailure, ::handleUpdatePdv) }

    private fun handleUpdatePdv(success:Boolean) {
        _successUpdatePdv.value = success
    }

}