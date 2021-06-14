package com.tawa.allinapp.core.di

import com.tawa.allinapp.AndroidApplication
import com.tawa.allinapp.core.di.viewmodel.ViewModelModule
import com.tawa.allinapp.features.auth.ui.LoginFragment
import com.tawa.allinapp.features.HomeActivity
import com.tawa.allinapp.features.auth.ui.LoginActivity
import com.tawa.allinapp.features.calendar.CalendarFragment
import com.tawa.allinapp.features.catalog.CatalogFragment
import com.tawa.allinapp.features.home.HomeFragment
import com.tawa.allinapp.features.location.LocationFragment
import com.tawa.allinapp.features.movies.ui.MoviesFragment
import com.tawa.allinapp.features.reports.ReportsFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class, ViewModelModule::class])
interface ApplicationComponent{
    fun inject(application: AndroidApplication)
    fun inject(homeActivity: HomeActivity)
    fun inject(loginActivity: LoginActivity)
    fun inject(moviesFragment: MoviesFragment)
    fun inject(locationFragment: LocationFragment)
    fun inject(catalogFragment: CatalogFragment)
    fun inject(calendarFragment: CalendarFragment)
    fun inject(reportsFragment: ReportsFragment)
    fun inject(loginFragment: LoginFragment)
    fun inject(homeFragment: HomeFragment)
}