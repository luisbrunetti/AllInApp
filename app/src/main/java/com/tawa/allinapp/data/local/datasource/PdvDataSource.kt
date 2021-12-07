package com.tawa.allinapp.data.local.datasource

import com.tawa.allinapp.data.local.dao.CheckDao
import com.tawa.allinapp.data.local.dao.PdvDao
import com.tawa.allinapp.data.local.models.CheckModel
import com.tawa.allinapp.data.local.models.PdvModel
import javax.inject.Inject

class PdvDataSource @Inject constructor(private val pdvDao: PdvDao){
    fun insertPdv(pdvModel: PdvModel){
        pdvDao.insertPdv(pdvModel)
    }
    fun deletePdvs(){
        pdvDao.deletePdvs()
    }

    fun getPdv (idPdv:String,idUser: String):PdvModel{
        return pdvDao.getPdv(idPdv,idUser)
    }

    fun getCountPdvIdUserNIdPv(idPdv:String, idUser: String): Int{
        return pdvDao.getCountPdvIdUserNIdPv(idPdv,idUser)
    }

    fun getPdvWithOutIdUser(idPdv:String): PdvModel{
        return pdvDao.getPdvWithOutIdUser(idPdv)
    }
    fun updatePdv( idPdv: String, idUser:String, nameUser: String, phoneUser: String, ruc: String, latitude: String, longitude: String, image: String, state: String){
        pdvDao.updatePdv(idPdv,idUser, nameUser,phoneUser,ruc,latitude,longitude,image,state)
    }

    fun updatePdv( idPdv: String,state: String){
        pdvDao.updatePdv(idPdv,state)
    }

}