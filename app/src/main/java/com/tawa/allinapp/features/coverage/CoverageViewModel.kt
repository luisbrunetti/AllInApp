package com.tawa.allinapp.features.coverage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tawa.allinapp.core.platform.BaseViewModel
import javax.inject.Inject


class CoverageViewModel
@Inject constructor() : BaseViewModel() {

    private val _text = MutableLiveData<String>("Coverage")
    val text: LiveData<String>
        get()= _text

}