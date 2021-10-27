package com.tawa.allinapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tawa.allinapp.data.local.models.CompanyModel
import com.tawa.allinapp.data.local.models.LanguageModel
import com.tawa.allinapp.data.local.models.ScheduleModel
import com.tawa.allinapp.data.local.models.TranslateModel
import com.tawa.allinapp.models.TranslateItem

@Dao
interface ParametersDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertCompanies(company: CompanyModel)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertPV(scheduleModel: ScheduleModel)

    @Query("SELECT * FROM company where idUser=:idUser")
    fun getCompanies(idUser:String): List<CompanyModel>

    @Query("SELECT * FROM schedule WHERE idCompany=:company and idUser=:idUser")
    fun getPV(company:String,idUser: String): List<ScheduleModel>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertTranslateItem(translateModel: TranslateModel)

    @Query("SELECT * FROM translate")
    fun getTranslate() : List<TranslateModel>
}