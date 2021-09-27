package com.tawa.allinapp.features.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tawa.allinapp.core.interactor.UseCase
import com.tawa.allinapp.core.platform.BaseViewModel
import com.tawa.allinapp.data.local.Prefs
import com.tawa.allinapp.features.auth.usecase.DoLogin
import com.tawa.allinapp.features.auth.usecase.GetCompaniesRemote
import javax.inject.Inject

class AuthViewModel
@Inject constructor(
    private val doLogin: DoLogin,
    private val getCompaniesRemote: GetCompaniesRemote,
    private val pref: Prefs
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

    private val _enableButton = MutableLiveData(false)
    val enableButton: LiveData<Boolean>
        get() = _enableButton

    private val _errorMessage = MutableLiveData("")
    val errorMessage = _errorMessage

    private val _errorEdits = MutableLiveData(false)
    val errorEdits: LiveData<Boolean>
        get() = _errorEdits

    private val _username = MutableLiveData("")
    val username = _username

    private val _password = MutableLiveData("")
    val password = _password


    fun setErrorLogin(error:String){
        _errorEdits.value = true
        _errorMessage.value = error
    }

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


    private fun handlePVRemote(success: Boolean) {
        this._successGetPV.value = success
    }

    fun validateFields() {
        _enableButton.postValue(_username.value!!.isNotEmpty() && _password.value!!.isNotEmpty())
    }

    fun endLogin(){
        if(_successGetCompanies.value==true)
            _successEndLogin.value = true
    }

    fun setSession(value: Boolean){
        pref.session = value
    }
    fun setSessionFirstTime(){
        if(!pref.session)
        pref.session = false
    }

}