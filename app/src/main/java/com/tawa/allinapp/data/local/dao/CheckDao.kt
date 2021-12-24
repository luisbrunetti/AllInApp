package com.tawa.allinapp.data.local.dao

import androidx.room.*
import com.tawa.allinapp.data.local.models.CheckInHistory
import com.tawa.allinapp.data.local.models.CheckModel
import com.tawa.allinapp.features.init.usecase.GetIdCompany

@Dao
interface CheckDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertCheck(checkIn: CheckModel)

    @Query("SELECT COUNT(id) FROM `check`WHERE pv=:idPv and idUser=:idUser")
    fun getStateCheck(idPv:String,idUser:String): Int

    @Query("SELECT * FROM `check` WHERE state='no enviado' and idUser=:idUser")
    fun getChecks(idUser: String): List<CheckModel>

    @Query("UPDATE `check` set state =:state where schedule=:schedule and pv=:pv and company=:idCompany and idUser=:idUser and comment=:type")
    fun updateCheck(schedule:String,pv:String,idCompany: String,idUser: String,type:String,state:String)

    @Query("UPDATE `check` set state =:state ")
    fun updateCheck(state:String)

    //Check History
    @Query("SELECT * FROM CheckInHistory WHERE idUser = :idUser and idPv = :idPv and idCompany= :idCompany")
    fun getCheckInHistory(idUser: String, idPv: String, idCompany: String) : CheckInHistory

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCheckInHistory(checkInHistory: CheckInHistory)

    @Query("UPDATE CheckInHistory set pending = :pending WHERE idUser = :idUser and idPv = :idPv and idCompany= :idCompany")
    fun updateCheckInHistory(idUser: String, idPv: String, idCompany: String, pending: String)

    @Query("SELECT count(*) FROM CheckInHistory WHERE idUser = :idUser and idPv = :idPv and idCompany= :idCompany")
    fun countCheckInHistory(idUser: String, idPv: String, idCompany: String) : Int

    @Query("SELECT * FROM CheckInHistory WHERE idUser = :idUser and idCompany = :idCompany and pending = 'PENDING' ")
    fun getOnPendingCheckIns(idUser: String, idCompany: String): CheckInHistory?
}