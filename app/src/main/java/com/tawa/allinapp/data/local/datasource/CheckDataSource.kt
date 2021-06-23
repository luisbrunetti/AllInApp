package com.tawa.allinapp.data.local.datasource

import com.tawa.allinapp.data.local.dao.CheckDao
import com.tawa.allinapp.data.local.models.CheckModel
import javax.inject.Inject

class CheckDataSource @Inject constructor(private val checkDao: CheckDao){
    fun insertCheck(check: CheckModel){
        checkDao.insertCheck(check)
    }

    fun getStateCheck(idPv:String):Int{
        return checkDao.getStateCheck(idPv)
    }
}