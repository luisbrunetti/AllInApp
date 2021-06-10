package com.tawa.allinapp

import android.app.Application
import com.tawa.allinapp.core.di.ApplicationComponent
import com.tawa.allinapp.core.di.ApplicationModule
import com.tawa.allinapp.core.di.DaggerApplicationComponent
import com.tawa.allinapp.data.local.Prefs

class AndroidApplication : Application() {

    lateinit var prefs: Prefs

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