package com.tawa.allinapp.data.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "company")
data class CompanyModel(
    @PrimaryKey val id: String,
    val code: String,
    val ruc: String,
    val name: String,
    val description: String,
)