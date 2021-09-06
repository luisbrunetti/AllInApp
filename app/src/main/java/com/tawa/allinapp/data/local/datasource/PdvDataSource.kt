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

    fun getPdv (idPdv:String):PdvModel{
        return  pdvDao.getPdv(idPdv)
    }

}