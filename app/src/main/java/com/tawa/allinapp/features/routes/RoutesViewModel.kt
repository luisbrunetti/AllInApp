package com.tawa.allinapp.features.routes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tawa.allinapp.core.interactor.UseCase
import com.tawa.allinapp.core.platform.BaseViewModel
import com.tawa.allinapp.features.routes.usercase.GetListUser
import com.tawa.allinapp.models.RoutesUser
import javax.inject.Inject

class RoutesViewModel
@Inject constructor(
    private val getListUser: GetListUser

) : BaseViewModel() {

    private val _text = MutableLiveData<String>("Rutas")
    val text: LiveData<String>
        get()= _text

    private val _listUser= MutableLiveData<List<RoutesUser>>()
    val listUser: LiveData<List<RoutesUser>>
        get()= _listUser


    fun getListUser() = getListUser(UseCase.None()) { it.either(::handleFailure, ::handleListUser) }

    private fun handleListUser(listUser : List<RoutesUser>) {
        _listUser.value = listUser
    }
}