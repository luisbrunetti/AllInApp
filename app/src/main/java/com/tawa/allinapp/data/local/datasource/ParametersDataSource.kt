package com.tawa.allinapp.data.local.datasource

import com.tawa.allinapp.data.local.dao.ParametersDao
import com.tawa.allinapp.data.local.models.CompanyModel
import com.tawa.allinapp.data.local.models.ScheduleModel
import javax.inject.Inject


class ParametersDataSource @Inject constructor(private val parametersDao: ParametersDao){
    fun insertCompanies(company: CompanyModel){
        parametersDao.insertCompanies(company)
    }
    fun insertPV(schedule: ScheduleModel){
        parametersDao.insertPV(schedule)
    }
    fun getCompanies(idUser:String):List<CompanyModel> {
        return parametersDao.getCompanies(idUser)
    }
    fun getPV(company:String,idUser: String):List<ScheduleModel> {
        return parametersDao.getPV(company,idUser)
    }
}