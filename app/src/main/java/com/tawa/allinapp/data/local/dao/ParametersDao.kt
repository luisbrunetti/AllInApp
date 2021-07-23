package com.tawa.allinapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tawa.allinapp.data.local.models.CompanyModel
import com.tawa.allinapp.data.local.models.ScheduleModel

@Dao
interface ParametersDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertCompanies(company: CompanyModel)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertPV(scheduleModel: ScheduleModel)

    @Query("SELECT * FROM company")
    fun getCompanies(): List<CompanyModel>

    @Query("SELECT * FROM schedule WHERE idCompany=:company")
    fun getPV(company:String): List<ScheduleModel>
}