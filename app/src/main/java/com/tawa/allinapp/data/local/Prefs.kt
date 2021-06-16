package com.tawa.allinapp.data.local

import android.content.Context
import javax.inject.Inject

class Prefs
@Inject constructor(context: Context){

    companion object{
        const val FILENAME ="dagger"
        const val USERNAME = "name"
        const val TOKEN = "token"
        const val SESSION = "session"
    }

    private val prefs  = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE)
    var name:String?
        get() = prefs.getString(USERNAME,"")
        set(value) = prefs.edit().putString(USERNAME,value).apply()

    var token:String?
        get() = prefs.getString(TOKEN,"")
        set(value) = prefs.edit().putString(TOKEN,value).apply()

    var session:Boolean
        get() = prefs.getBoolean(SESSION,false)
        set(value) = prefs.edit().putBoolean(SESSION,value).apply()
}