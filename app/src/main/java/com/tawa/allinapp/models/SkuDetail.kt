package com.tawa.allinapp.models

data class SkuDetail(
    val id:String ,
    val idPv:String,
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
)