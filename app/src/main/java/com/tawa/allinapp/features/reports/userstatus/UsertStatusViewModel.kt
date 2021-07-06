package com.tawa.allinapp.features.reports.userstatus
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tawa.allinapp.core.platform.BaseViewModel
import javax.inject.Inject

class UserStatusViewModel
@Inject constructor() : BaseViewModel() {

    private val _text = MutableLiveData<String>("USER STATUS")
    val text: LiveData<String>
        get()= _text
}