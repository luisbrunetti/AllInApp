package com.tawa.allinapp.data.local

import android.content.Context
import javax.inject.Inject

class Prefs
@Inject constructor(context: Context){

    companion object{
        const val FILENAME ="allin"
        const val USERNAME = "name"
        const val USER = "user"
        const val SUPERVISOR = "supervisor"
        const val TOKEN = "token"
        const val USERID = "idUser"
        const val SESSION = "session"
        const val CHECK_IN = "checkIn"
        const val COMPANY_ID= "companyId"
        const val PV_ID = "pvId"
        const val PV_NAME = "pv_name"
    }

    private val prefs  = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE)
    var name:String?
        get() = prefs.getString(USERNAME,"")
        set(value) = prefs.edit().putString(USERNAME,value).apply()

    var user:String?
        get() = prefs.getString(USER,"")
        set(value) = prefs.edit().putString(USER,value).apply()

    var role:String?
        get() = prefs.getString(SUPERVISOR,"")
        set(value) = prefs.edit().putString(SUPERVISOR,value).apply()

    var idUser:String?
        get() = prefs.getString(USERID,"")
        set(value) = prefs.edit().putString(USERID,value).apply()

    var token:String?
        get() = prefs.getString(TOKEN,"")
        set(value) = prefs.edit().putString(TOKEN,value).apply()

    var checkIn:Boolean
        get() = prefs.getBoolean(CHECK_IN,true)
        set(value) = prefs.edit().putBoolean(CHECK_IN,value).apply()

    var companyId:String?
        get() = prefs.getString(COMPANY_ID,"")
        set(value) = prefs.edit().putString(COMPANY_ID,value).apply()

    var pvId:String?
        get() = prefs.getString(PV_ID,"")
        set(value) = prefs.edit().putString(PV_ID,value).apply()

    var pvName:String?
        get() = prefs.getString(PV_NAME,"")
        set(value) = prefs.edit().putString(PV_NAME,value).apply()

    var session:Boolean
        get() = prefs.getBoolean(SESSION,false)
        set(value) = prefs.edit().putBoolean(SESSION,value).apply()
}