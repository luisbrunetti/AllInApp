package com.tawa.allinapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tawa.allinapp.data.local.models.CheckinModel
import com.tawa.allinapp.data.local.models.CompanyModel
import com.tawa.allinapp.data.local.models.PVModel

@Dao
interface ParametersDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertCompanies(company: CompanyModel)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertPV(pvModel: PVModel)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertCheckin(checkin: CheckinModel)

    @Query("SELECT * FROM company")
    fun getCompanies(): List<CompanyModel>

    @Query("SELECT * FROM pv")
    fun getPV(): List<PVModel>
}