package com.tawa.allinapp.features.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tawa.allinapp.core.platform.BaseViewModel
import javax.inject.Inject

class HomeViewModel
    @Inject constructor ():BaseViewModel() {

    private val _text = MutableLiveData<String>("INICIO")
    val text: LiveData<String>
        get()= _text
}