package com.tawa.allinapp.data.local.datasource

import com.tawa.allinapp.data.local.dao.ParametersDao
import com.tawa.allinapp.data.local.models.CheckInModel
import com.tawa.allinapp.data.local.models.CompanyModel
import com.tawa.allinapp.data.local.models.PVModel
import javax.inject.Inject


class ParametersDataSource @Inject constructor(private val parametersDao: ParametersDao){
    fun insertCompanies(company: CompanyModel){
        parametersDao.insertCompanies(company)
    }
    fun insertPV(pv: PVModel){
        parametersDao.insertPV(pv)
    }
    fun getCompanies():List<CompanyModel> {
        return parametersDao.getCompanies()
    }
    fun getPV(company:String):List<PVModel> {
        return parametersDao.getPV(company)
    }
}