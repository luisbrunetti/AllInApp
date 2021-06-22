package com.tawa.allinapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tawa.allinapp.data.local.models.CheckInModel
import com.tawa.allinapp.data.local.models.CompanyModel
import com.tawa.allinapp.data.local.models.PVModel

@Dao
interface ParametersDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertCompanies(company: CompanyModel)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertPV(pvModel: PVModel)

    @Query("SELECT * FROM company")
    fun getCompanies(): List<CompanyModel>

    @Query("SELECT * FROM pv WHERE idCompany=:company")
    fun getPV(company:String): List<PVModel>
}