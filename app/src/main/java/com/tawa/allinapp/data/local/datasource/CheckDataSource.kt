package com.tawa.allinapp.data.local.datasource

import com.tawa.allinapp.data.local.dao.CheckDao
import com.tawa.allinapp.data.local.dao.ParametersDao
import com.tawa.allinapp.data.local.models.CheckInModel
import com.tawa.allinapp.data.local.models.CompanyModel
import com.tawa.allinapp.data.local.models.PVModel
import javax.inject.Inject


class CheckDataSource @Inject constructor(private val parametersDao: CheckDao){
    fun insertCheckIn(checkIn: CheckInModel){
        parametersDao.insertCheckIn(checkIn)
    }

}