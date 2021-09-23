package com.tawa.allinapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tawa.allinapp.data.local.models.CheckModel
import com.tawa.allinapp.features.init.usecase.GetIdCompany

@Dao
interface CheckDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertCheck(checkIn: CheckModel)

    @Query("SELECT COUNT(id) FROM `check`WHERE pv=:idPv and idUser=:idUser")
    fun getStateCheck(idPv:String,idUser:String): Int

    @Query("SELECT * FROM `check` WHERE state='no enviado'")
    fun getChecks(): List<CheckModel>

    @Query("UPDATE `check` set state =:state where schedule=:schedule and pv=:pv and company=:idCompany and idUser=:idUser and comment=:type")
    fun updateCheck(schedule:String,pv:String,idCompany: String,idUser: String,type:String,state:String)

    @Query("UPDATE `check` set state =:state ")
    fun updateCheck(state:String)
}