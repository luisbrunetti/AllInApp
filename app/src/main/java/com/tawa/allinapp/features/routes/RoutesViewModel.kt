package com.tawa.allinapp.features.routes

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tawa.allinapp.core.interactor.UseCase
import com.tawa.allinapp.core.platform.BaseViewModel
import com.tawa.allinapp.features.init.usecase.GetRoleUser
import com.tawa.allinapp.features.routes.usercase.GetListUser
import com.tawa.allinapp.features.routes.usercase.GetRoutes
import com.tawa.allinapp.features.routes.usercase.GetTracking
import com.tawa.allinapp.models.Routes
import com.tawa.allinapp.models.RoutesUser
import com.tawa.allinapp.models.Tracking
import javax.inject.Inject

class RoutesViewModel
@Inject constructor(
    private val getListUser: GetListUser,
    private val getRoutes: GetRoutes,
    private val getTracking: GetTracking,
    private val getRoleUser: GetRoleUser

) : BaseViewModel() {

    private val _text = MutableLiveData<String>("Rutas")
    val text: LiveData<String>
        get()= _text

    private val _listUser= MutableLiveData<List<RoutesUser>>()
    val listUser: LiveData<List<RoutesUser>>
        get()= _listUser

    private val _successGetRoutes= MutableLiveData<List<Routes>>()
    val successGetRoutes: LiveData<List<Routes>>
        get()= _successGetRoutes

    private val _successGetTracking= MutableLiveData<List<Tracking>>()
    val successGetTracking: LiveData<List<Tracking>>
        get()= _successGetTracking

    private val _successGetRole = MutableLiveData("")
    val successGetRole: LiveData<String>
        get() = _successGetRole


    fun getListUser() = getListUser(UseCase.None()) { it.either(::handleFailure, ::handleListUser) }

    private fun handleListUser(listUser : List<RoutesUser>) {
        _listUser.value = listUser
    }

    fun getRoutes(idUser:String,dateStart:String,type:Int) = getRoutes(GetRoutes.Params(idUser,dateStart,type)) { it.either(::handleFailure, ::handleGetRoutes) }

    private fun handleGetRoutes(listRoutes : List<Routes>) {
        _successGetRoutes.value = listRoutes
    }

    fun getTracking(idUser:String,dateStart:String,type:Int) = getTracking(GetTracking.Params(idUser,dateStart,type)) {
        it.either(::handleFailure, ::handleGetTracking)
    }

    private fun handleGetTracking(listTracking : List<Tracking>) {
        Log.d("tracking", listTracking.toString())
        _successGetTracking.value = listTracking
    }

    fun getRoleUser() = getRoleUser(UseCase.None()) { it.either(::handleFailure, ::handleGetRole) }

    private fun handleGetRole(role:String) { this._successGetRole.value = role }
}