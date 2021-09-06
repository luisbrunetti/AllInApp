package com.tawa.allinapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tawa.allinapp.data.local.models.CompanyModel
import com.tawa.allinapp.data.local.models.PdvModel
import com.tawa.allinapp.data.local.models.ScheduleModel

@Dao
interface PdvDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertPdv(pdvModel: PdvModel)

    @Query("SELECT * FROM pdv WHERE id=:idPdv")
    fun getPdv(idPdv:String):PdvModel


}