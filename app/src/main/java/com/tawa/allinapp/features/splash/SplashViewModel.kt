package com.tawa.allinapp.features.splash

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tawa.allinapp.core.interactor.UseCase
import com.tawa.allinapp.core.platform.BaseViewModel
import com.tawa.allinapp.data.local.Prefs
import java.util.prefs.Preferences
import javax.inject.Inject

class SplashViewModel
@Inject constructor(
    private val getSession: GetSession,
    private val preferences: Prefs
): BaseViewModel(){

    private val _session = MutableLiveData<Boolean>()
    val session: LiveData<Boolean> get() = _session

    fun getSession() = getSession(UseCase.None()) {
        it.either(::handleFailure, ::handleSession)
    }

    private fun handleSession(success: Boolean) {
        this._session.value = success
    }

    fun getSessionPrefs(): Boolean{
        return preferences.session
    }
}