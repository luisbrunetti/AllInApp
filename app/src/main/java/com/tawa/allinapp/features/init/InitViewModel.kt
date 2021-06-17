package com.tawa.allinapp.features.init

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tawa.allinapp.core.platform.BaseViewModel
import com.tawa.allinapp.features.auth.usecase.DoLogin
import javax.inject.Inject

class InitViewModel
@Inject constructor(
    private val getCompanies: GetCompanies,
    private val getPV: GetPV,
) : BaseViewModel() {

    private val _text = MutableLiveData<String>("Inicio")
    val text: LiveData<String>
        get()= _text
}