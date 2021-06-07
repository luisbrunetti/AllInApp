package com.tawa.allinapp.data.local

import android.content.Context
import javax.inject.Inject

class Prefs
@Inject constructor(context: Context){

    companion object{
        const val FILENAME ="dagger"
        const val USERNAME = "name"
        const val AGE = "age"
    }

    private val prefs  = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE)
    var name:String?
        get() = prefs.getString(USERNAME,"")
        set(value) = prefs.edit().putString(USERNAME,value).apply()

    var age:Int
        get() = prefs.getInt(AGE,-1)
        set(value) = prefs.edit().putInt(AGE,value).apply()
}