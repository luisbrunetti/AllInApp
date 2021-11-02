package com.tawa.allinapp.core.platform

import com.tawa.allinapp.models.Translate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TranslateObject
@Inject constructor() {

    companion object var LANGUAGE = 0

    private var translate : Translate = Translate(arrayListOf())

    fun setInstance(translate: Translate) {
        this.translate = translate
    }

    fun getInstance(): Translate{
        return this.translate
    }

    fun findTranslate(id:String): String? {
        translate.arrayTranslate.find { it.id == id }
            .also { return it?.translate?.get(LANGUAGE) }
    }
}