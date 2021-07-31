package com.tawa.allinapp.data.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.tawa.allinapp.models.Report
import com.tawa.allinapp.models.Sku
import com.tawa.allinapp.models.SkuDetail

@Entity(tableName = "sku_detail")
data class SkuDetailModel(
    @PrimaryKey val id:String ,
    val dateCreation :String,
    val idProduct:String,
    val nameProduct:String,
    val category:String,
    val subCategory:String,
    val inventory:Int,
    val price:Float,
    val stock:Boolean,
    val exhibition:Boolean,
    val newPrice:Float,
    val idSku:String
){
    fun toView() = SkuDetail(id,dateCreation,idProduct,nameProduct,category,subCategory,inventory,price,stock,exhibition,newPrice,idSku)
}