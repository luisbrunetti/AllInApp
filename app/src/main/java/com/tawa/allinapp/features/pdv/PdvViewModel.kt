package com.tawa.allinapp.features.pdv

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import javax.inject.Inject

class PdvViewModel
    @Inject constructor() : ViewModel() {

    private val _text = MutableLiveData<String>("Calendario")
    val text: LiveData<String>
        get()= _text

}