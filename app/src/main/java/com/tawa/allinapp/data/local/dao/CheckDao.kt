package com.tawa.allinapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tawa.allinapp.data.local.models.CheckModel

@Dao
interface CheckDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertCheck(checkIn: CheckModel)

    @Query("SELECT COUNT(id) FROM `check`WHERE pv=:idPv")
    fun getStateCheck(idPv:String): Int
}