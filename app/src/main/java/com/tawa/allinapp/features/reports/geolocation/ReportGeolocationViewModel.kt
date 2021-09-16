package com.tawa.allinapp.features.reports.geolocation

import android.app.DatePickerDialog
import android.util.Log
import android.widget.EditText
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tawa.allinapp.core.interactor.UseCase
import com.tawa.allinapp.core.platform.BaseViewModel
import com.tawa.allinapp.features.reports.geolocation.usecase.GetRoutesFromListUsers
import com.tawa.allinapp.features.reports.geolocation.usecase.GetTrackingFromListUsers
import com.tawa.allinapp.features.reports.picture.DeletePhotoReports
import com.tawa.allinapp.features.reports.picture.usecase.GetLocalPhotoReport
import com.tawa.allinapp.features.routes.usercase.GetListUser
import com.tawa.allinapp.models.Routes
import com.tawa.allinapp.models.RoutesInform
import com.tawa.allinapp.models.RoutesUser
import com.tawa.allinapp.models.TrackingInform
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject

class ReportGeolocationViewModel @Inject constructor(
    private val getListUser: GetListUser,
    private val getRoutesFromListUsers: GetRoutesFromListUsers,
    private val getTrackingFromListUsers: GetTrackingFromListUsers
): BaseViewModel() {

    private val _getUserList = MutableLiveData<List<RoutesUser>>()
    val getUserList : LiveData<List<RoutesUser>> get() = _getUserList

    private val _getRouteUsers = MutableLiveData<List<TrackingInform>>()
    val getRouteUsers : LiveData<List<TrackingInform>> get() = _getRouteUsers

    fun getListUser() = getListUser(UseCase.None()){ it.either(::handleFailure, ::handleListUser)}
    private fun handleListUser(userList: List<RoutesUser>){
        Log.d("userlist", userList.toString())
        this._getUserList.value = userList
    }

    fun getRoutesFromListUsers(mutableListUser : MutableList<RoutesUser>, dateStart: String) {
        getTrackingFromListUsers(GetTrackingFromListUsers.Params(mutableListUser,dateStart)) { it.either(::handleFailure, ::handleRoutesFromListUser) }
    }

    private fun handleRoutesFromListUser(list: List<TrackingInform>){
        Log.d("list->",list.toString())
        this._getRouteUsers.value = list
    }

    fun getCurrentDay(): Triple<Int,Int,Int>{
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        return Triple(day,month,year)
    }
    fun convertDate(date:String):String{
        val inputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ENGLISH)
        val outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH)
        val dateTime = LocalDate.parse(date,inputFormatter)
        return outputFormatter.format(dateTime)
    }

}