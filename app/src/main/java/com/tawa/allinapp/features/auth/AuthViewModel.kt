package com.tawa.allinapp.features.auth

import androidx.lifecycle.MutableLiveData
import com.tawa.allinapp.core.platform.BaseViewModel
import com.tawa.allinapp.features.auth.usecase.DoLogin
import javax.inject.Inject

class AuthViewModel
@Inject constructor(private val doLogin: DoLogin): BaseViewModel(){

    var success: MutableLiveData<Boolean> = MutableLiveData()

    fun login(username: String, password: String) = doLogin(DoLogin.Params(username, password)) {
        it.either(::handleFailure, ::handleLogin)
    }

    private fun handleLogin(success: Boolean) {
        this.success.value = success
    }
}