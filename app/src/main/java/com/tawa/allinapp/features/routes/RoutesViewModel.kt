package com.tawa.allinapp.features.routes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tawa.allinapp.core.platform.BaseViewModel
import javax.inject.Inject

class RoutesViewModel
@Inject constructor() : BaseViewModel() {

    private val _text = MutableLiveData<String>("Rutas")
    val text: LiveData<String>
        get()= _text
}