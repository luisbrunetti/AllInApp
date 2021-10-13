package com.tawa.allinapp.core.platform

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tawa.allinapp.core.functional.Failure

abstract class BaseViewModel : ViewModel() {

    var failure: MutableLiveData<Failure> = MutableLiveData()



    protected fun handleFailure(failure: Failure) {
        this.failure.value = failure
    }
}