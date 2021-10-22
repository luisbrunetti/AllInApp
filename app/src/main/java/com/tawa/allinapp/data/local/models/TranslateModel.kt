package com.tawa.allinapp.data.local.models
import androidx.room.Entity
import com.google.gson.annotations.SerializedName

@Entity(tableName = "translate",primaryKeys = ["id"])
data class TranslateModel(
    val id: String, // tvHellowLoginFragment
    val translate: String
)