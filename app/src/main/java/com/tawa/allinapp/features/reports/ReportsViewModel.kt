package com.tawa.allinapp.features.reports

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tawa.allinapp.core.platform.BaseViewModel
import javax.inject.Inject

class ReportsViewModel
    @Inject constructor ():BaseViewModel() {

    private val _text = MutableLiveData<String>("Reportes")
    val text: LiveData<String>
        get()= _text
}