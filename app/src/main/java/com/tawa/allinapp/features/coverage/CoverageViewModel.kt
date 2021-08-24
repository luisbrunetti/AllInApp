package com.tawa.allinapp.features.coverage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tawa.allinapp.core.interactor.UseCase
import com.tawa.allinapp.core.platform.BaseViewModel
import com.tawa.allinapp.models.Chain
import com.tawa.allinapp.models.Channel
import com.tawa.allinapp.models.Retail
import com.tawa.allinapp.models.User
import javax.inject.Inject

class CoverageViewModel
@Inject constructor(
    private val getChannels: GetChannels,
    private val getRetails: GetRetails,
    private val getChains: GetChains,
    private val getUserList: GetUserList,
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

    private val _chains = MutableLiveData<List<Chain>>()
    val chains: LiveData<List<Chain>>
        get()= _chains

    private val _userList = MutableLiveData<List<User>>()
    val userList: LiveData<List<User>>
        get()= _userList

    fun getChannels() = getChannels(UseCase.None()){ it.either(::handleFailure, ::handleChannels) }
    fun getRetails() = getRetails(UseCase.None()){ it.either(::handleFailure, ::handleRetails) }
    fun getChains(channel: String, retail: String) = getChains(GetChains.Params(channel,retail)){ it.either(::handleFailure, ::handleChains) }
    fun getUserList() = getUserList(UseCase.None()){ it.either(::handleFailure, ::handleUserList) }

    fun onTextChanged(text:String){
        _text.value = text
    }

    private fun handleChannels(channels:List<Channel>){
        _channels.value = channels
    }

    private fun handleRetails(retails:List<Retail>){
        _retails.value = retails
    }

    private fun handleChains(chains:List<Chain>){
        _chains.value = chains
    }

    private fun handleUserList(userList:List<User>){
        _userList.value = userList
    }
}