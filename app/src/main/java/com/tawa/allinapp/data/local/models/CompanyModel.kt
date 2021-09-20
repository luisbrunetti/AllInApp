package com.tawa.allinapp.data.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.tawa.allinapp.models.Company

@Entity(tableName = "company",primaryKeys = ["id","idUser"])
data class CompanyModel(
    val id: String,
    val ruc: String,
    val name: String,
    val description: String,
    val idUser:String,
    val image:String
){
    fun toView() = Company(id,ruc,name,description,idUser,image)
}