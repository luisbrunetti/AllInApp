package com.tawa.allinapp.data.local.datasource

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tawa.allinapp.data.local.dao.CheckDao
import com.tawa.allinapp.data.local.models.CheckInHistory
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

    fun getChecks(idUser: String):List<CheckModel>{
        return checkDao.getChecks(idUser)
    }

    fun updateCheck(schedule:String,pv:String,idCompany: String,idUser: String,type:String,state:String){
        checkDao.updateCheck(schedule,pv,idCompany,idUser,type,state)
    }

    fun updateCheck(state:String){
        checkDao.updateCheck(state)
    }

    //Check History
    fun getCheckInHistory(idUser: String, idPv: String, idCompany: String): CheckInHistory{
        return checkDao.getCheckInHistory(idUser,idPv,idCompany)
    }

    fun insertCheckInHistory(checkInHistory: CheckInHistory){
        checkDao.insertCheckInHistory(checkInHistory)
    }

    fun updateCheckInHistory(idUser: String, idPv: String, idCompany: String, pending: String){
        checkDao.updateCheckInHistory(idUser,idPv,idCompany,pending)
    }

    fun countCheckInHistory(idUser: String, idPv: String, idCompany: String): Int{
        return checkDao.countCheckInHistory(idUser,idPv,idCompany)
    }

    fun getOnPendingCheckIns(idUser: String, idCompany: String): CheckInHistory? {
        return checkDao.getOnPendingCheckIns(idUser,idCompany)
    }


}