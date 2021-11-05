package com.tawa.allinapp.core.platform

import com.tawa.allinapp.models.TranslateItem
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TranslateObject
@Inject constructor() {

    companion object var LANGUAGE = 0

    private var translate : List<TranslateItem> = arrayListOf()

    fun setInstance(translate: List<TranslateItem>) {
        this.translate = translate
    }

    fun getInstance(): List<TranslateItem>{
        return this.translate
    }

    fun findTranslate(id:String): String? {
        translate.find { it.id == id }
            .also { return it?.translate?.get(LANGUAGE) }
    }
}