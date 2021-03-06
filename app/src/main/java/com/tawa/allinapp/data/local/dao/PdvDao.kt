package com.tawa.allinapp.data.local.dao

import androidx.room.*
import com.tawa.allinapp.data.local.models.CompanyModel
import com.tawa.allinapp.data.local.models.PdvModel
import com.tawa.allinapp.data.local.models.ScheduleModel

@Dao
interface PdvDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertPdv(pdvModel: PdvModel)

    @Query("DELETE FROM pdv")
    fun deletePdvs()

    @Query("SELECT * FROM pdv WHERE id=:idPdv and idUser =:idUser ")
    fun getPdv(idPdv:String, idUser: String):PdvModel

    @Query("SELECT count(*) FROM pdv WHERE id=:idPdv and idUser =:idUser")
    fun getCountPdvIdUserNIdPv(idPdv:String, idUser: String): Int

    @Query("SELECT * FROM pdv WHERE id=:idPdv")
    fun getPdvWithOutIdUser(idPdv:String): PdvModel

    @Query("update pdv set idUser =:idUser, nameUser=:nameUser,phoneUser=:phoneUser,ruc=:ruc,latitude=:latitude,longitude=:longitude,image=:image,state=:state where id=:idPdv and idUser =:idUser" )
    fun updatePdv( idPdv: String,
                   idUser:String,
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