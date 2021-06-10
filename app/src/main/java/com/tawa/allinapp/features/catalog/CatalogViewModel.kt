package com.tawa.allinapp.features.catalog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tawa.allinapp.core.platform.BaseViewModel
import javax.inject.Inject

class CatalogViewModel
    @Inject constructor() : BaseViewModel() {

    private val _text = MutableLiveData<String>("Listados")
    val text: LiveData<String>
        get()= _text
}