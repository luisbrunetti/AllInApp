package com.tawa.allinapp.data.local.datasource

import com.tawa.allinapp.data.local.dao.CheckDao
import com.tawa.allinapp.data.local.models.CheckModel
import com.tawa.allinapp.features.init.usecase.GetIdCompany
import javax.inject.Inject

class CheckDataSource @Inject constructor(private val checkDao: CheckDao){
    fun insertCheck(check: CheckModel){
        checkDao.insertCheck(check)
    }

    fun getStateCheck(idPv:String,idUser: String):Int{
        return checkDao.getStateCheck(idPv,idUser)
    }

    fun getChecks():List<CheckModel>{
        return checkDao.getChecks()
    }

    fun updateCheck(schedule:String,pv:String,idCompany: String,idUser: String,type:String,state:String){
        checkDao.updateCheck(schedule,pv,idCompany,idUser,type,state)
    }
}