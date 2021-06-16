package com.tawa.allinapp.features.auth

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tawa.allinapp.core.interactor.UseCase
import com.tawa.allinapp.core.platform.BaseViewModel
import com.tawa.allinapp.features.auth.usecase.DoLogin
import com.tawa.allinapp.features.auth.usecase.GetCompaniesRemote
import com.tawa.allinapp.features.auth.usecase.GetPVRemote
import javax.inject.Inject

class AuthViewModel
@Inject constructor(
    private val doLogin: DoLogin,
    private val getCompaniesRemote: GetCompaniesRemote,
    private val getPVRemote: GetPVRemote,
): BaseViewModel(){

    private val _startLogin = MutableLiveData(false)
    val startLogin: LiveData<Boolean>
        get() = _startLogin

    private val _successLogin = MutableLiveData(false)
    val successLogin: LiveData<Boolean>
        get() = _successLogin

    private val _successEndLogin = MutableLiveData(false)
    val successEndLogin: LiveData<Boolean>
        get() = _successEndLogin

    private val _successGetCompanies = MutableLiveData(false)
    val successGetCompanies: LiveData<Boolean>
        get() = _successGetCompanies

    private val _successGetPV = MutableLiveData(false)
    val successGetPV: LiveData<Boolean>
        get() = _successGetPV

    private val _enableButton = MutableLiveData<Boolean>(false)
    val enableButton: LiveData<Boolean>
        get() = _enableButton

    private val _username = MutableLiveData("")
    val username = _username

    private val _password = MutableLiveData("")
    val password = _password

    private val _error = MutableLiveData("")
    val error = _error

    fun doLogin() {
        _startLogin.value = true
        doLogin(DoLogin.Params(_username.value!!,_password.value!!)) {
            it.either(::handleFailure, ::handleLogin)
        }
    }
    private fun handleLogin(success: Boolean) {
        this._successLogin.value = success
    }

    fun getCompaniesRemote() = getCompaniesRemote(UseCase.None()) {
        it.either(::handleFailure, ::handleCompaniesRemote)
    }
    private fun handleCompaniesRemote(success: Boolean) {
        this._successGetCompanies.value = success
    }

    fun getPVRemote() = getPVRemote(UseCase.None()) {
        it.either(::handleFailure, ::handlePVRemote)
    }
    private fun handlePVRemote(success: Boolean) {
        this._successGetPV.value = success
    }

    fun validateFields() {
        _enableButton.postValue(_username.value!!.isNotEmpty() && _password.value!!.isNotEmpty())
    }

    fun endLogin(){
        if(_successGetCompanies.value==true && _successGetPV.value==true)
            _successEndLogin.value = true
    }
}