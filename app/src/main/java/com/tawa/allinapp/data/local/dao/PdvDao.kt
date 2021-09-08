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

    @Query("update pdv set nameUser=:nameUser,phoneUser=:phoneUser,ruc=:ruc,latitude=:latitude,longitude=:longitude,image=:image,state=:state where id=:idPdv")
    fun updatePdv( idPdv: String,
                   nameUser: String,
                   phoneUser: String,
                   ruc: String,
                   latitude: String,
                   longitude: String,
                   image: String,
                   state: String)

    @Query("update pdv set state=:state where id=:idPdv")
    fun updatePdv( idPdv: String, state: String)


}