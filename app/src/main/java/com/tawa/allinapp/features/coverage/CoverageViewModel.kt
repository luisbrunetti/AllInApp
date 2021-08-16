package com.tawa.allinapp.features.coverage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tawa.allinapp.core.interactor.UseCase
import com.tawa.allinapp.core.platform.BaseViewModel
import com.tawa.allinapp.models.Channel
import com.tawa.allinapp.models.Retail
import javax.inject.Inject

class CoverageViewModel
@Inject constructor(
    private val getChannels: GetChannels,
    private val getRetails: GetRetails,
) : BaseViewModel() {

    private val _text = MutableLiveData<String>("")
    val text: LiveData<String>
        get()= _text

    private val _channels = MutableLiveData<List<Channel>>()
    val channels: LiveData<List<Channel>>
        get()= _channels

    private val _retails = MutableLiveData<List<Retail>>()
    val retails: LiveData<List<Retail>>
        get()= _retails

    fun getChannels() = getChannels(UseCase.None()){ it.either(::handleFailure, ::handleChannels) }
    fun getRetails() = getRetails(UseCase.None()){ it.either(::handleFailure, ::handleRetails) }

    fun onTextChanged(text:String){
        _text.value = text
    }

    private fun handleChannels(channels:List<Channel>){
        _channels.value = channels
    }

    private fun handleRetails(retails:List<Retail>){
        _retails.value = retails
    }
}