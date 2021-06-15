package com.tawa.allinapp.features.init

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tawa.allinapp.core.platform.BaseViewModel
import javax.inject.Inject

class InitViewModel
@Inject constructor() : BaseViewModel() {

    private val _text = MutableLiveData<String>("Inicio")
    val text: LiveData<String>
        get()= _text
}