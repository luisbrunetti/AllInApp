package com.tawa.allinapp.features.reports.picture

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tawa.allinapp.core.platform.BaseViewModel
import javax.inject.Inject

class PictureViewModel
@Inject constructor():BaseViewModel(){
    private val _text = MutableLiveData<String>("HOLAAAAAAA")
    val text: LiveData<String>
        get()= _text
}