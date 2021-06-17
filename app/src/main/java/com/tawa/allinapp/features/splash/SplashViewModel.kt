package com.tawa.allinapp.features.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tawa.allinapp.core.interactor.UseCase
import com.tawa.allinapp.core.platform.BaseViewModel
import javax.inject.Inject

class SplashViewModel
@Inject constructor(
    private val getSession: GetSession
): BaseViewModel(){

    private val _session = MutableLiveData(false)
    val session: LiveData<Boolean>
        get() = _session

    fun getSession() = getSession(UseCase.None()) {
        it.either(::handleFailure, ::handleSession)
    }

    private fun handleSession(success: Boolean) {
        this._session.value = success
    }
}