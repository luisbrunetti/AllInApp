package com.tawa.allinapp

import android.app.Application
import com.tawa.allinapp.core.di.ApplicationComponent
import com.tawa.allinapp.core.di.ApplicationModule
import com.tawa.allinapp.core.di.DaggerApplicationComponent
import com.tawa.allinapp.data.local.Prefs


class AndroidApplication : Application() {

    lateinit var prefs: Prefs

    companion object{
        private const val APPLICATION_ID = "94117"
        private const val AUTH_KEY = "yUwAMwfQTcU4Ogq"
        private const val AUTH_SECRET = "yjTBePJ2NYp2OxY"
        private const val ACCOUNT_KEY = "sxH9a7gXRRf4uFg8-7ra"
        private lateinit var instance: AndroidApplication
        fun getInstance(): AndroidApplication = instance
    }

    val appComponent: ApplicationComponent by lazy(mode = LazyThreadSafetyMode.NONE) {
        DaggerApplicationComponent
            .builder()
            .applicationModule(ApplicationModule(this))
            .build()
    }

    override fun onCreate() {
        super.onCreate()
        this.injectMembers()
        prefs = Prefs(applicationContext)
    }



    private fun injectMembers() = appComponent.inject(this)


}