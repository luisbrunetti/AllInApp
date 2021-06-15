package com.tawa.allinapp.features.informs

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tawa.allinapp.core.platform.BaseViewModel
import javax.inject.Inject

class InformsViewModel
    @Inject constructor() : BaseViewModel() {

    private val _text = MutableLiveData<String>("Informes")
    val text: LiveData<String>
        get()= _text
}