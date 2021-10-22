package com.tawa.allinapp.models
import com.google.gson.annotations.SerializedName
import com.tawa.allinapp.data.local.models.TranslateModel


data class Translate(
    @SerializedName("arrayData")
    val arrayTranslate : List<TranslateItem>
)

data class TranslateItem(
    @SerializedName("id")
    val id: String, // tvInfoLoginFragment
    @SerializedName("translate")
    val translate: List<String>
){
    fun toModel(translateString: String) = TranslateModel(id,translateString)
}
