package com.tawa.allinapp.data.local.datasource

import com.tawa.allinapp.data.local.dao.ParametersDao
import com.tawa.allinapp.data.local.models.CompanyModel
import com.tawa.allinapp.data.local.models.LanguageModel
import com.tawa.allinapp.data.local.models.ScheduleModel
import com.tawa.allinapp.data.local.models.TranslateModel
import com.tawa.allinapp.models.TranslateItem
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
    fun insertTranslateItem(translateModel: TranslateModel) {
        return parametersDao.insertTranslateItem(translateModel)
    }
    fun getTranslate(): List<TranslateModel>{
        return parametersDao.getTranslate()
    }
    fun getCountTranslate() : Int{
        return parametersDao.getCountTranslate()
    }
}