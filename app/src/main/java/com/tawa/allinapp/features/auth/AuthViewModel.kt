package com.tawa.allinapp.features.auth

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tawa.allinapp.core.platform.BaseViewModel
import com.tawa.allinapp.features.auth.usecase.DoLogin
import javax.inject.Inject

class AuthViewModel
@Inject constructor(private val doLogin: DoLogin): BaseViewModel(){

    var success: MutableLiveData<Boolean> = MutableLiveData()

    private val _successLogin = MutableLiveData<Boolean>(false)
    val successLogin: LiveData<Boolean>
        get() = _successLogin

    private val _username = MutableLiveData<String>()
    val username = _username

    private val _password = MutableLiveData<String>()
    val password = _password

    fun login(username:String, password:String) = doLogin(DoLogin.Params(username,password)) {
        it.either(::handleFailure, ::handleLogin)
    }

    private fun handleLogin(success: Boolean) {
        this._successLogin.value = success
    }

    fun onLogin() {
        Log.w("username",username.value?:"")
        Log.w("password",password.value?:"")
    }
}