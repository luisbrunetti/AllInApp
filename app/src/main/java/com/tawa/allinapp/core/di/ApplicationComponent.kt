package com.tawa.allinapp.core.di

import com.tawa.allinapp.AndroidApplication
import com.tawa.allinapp.core.di.viewmodel.ViewModelModule
import com.tawa.allinapp.features.init.ui.SelectorDialogFragment
import com.tawa.allinapp.features.auth.ui.LoginFragment
import com.tawa.allinapp.features.HomeActivity
import com.tawa.allinapp.features.auth.ui.LoginActivity
import com.tawa.allinapp.features.calendar.CalendarFragment
import com.tawa.allinapp.features.informs.PDVFragment
import com.tawa.allinapp.features.init.ui.CheckInDialogFragment
import com.tawa.allinapp.features.init.ui.InitFragment


import com.tawa.allinapp.features.location.LocationFragment
import com.tawa.allinapp.features.movies.ui.MoviesFragment
import com.tawa.allinapp.features.reports.audio.AudioFragment
import com.tawa.allinapp.features.reports.picture.PictureFragment
import com.tawa.allinapp.features.reports.standard.CheckListFragment
import com.tawa.allinapp.features.reports.reports.ReportsFragment
import com.tawa.allinapp.features.reports.sku.SkuFragment
import com.tawa.allinapp.features.reports.userstatus.LastLocationFragment
import com.tawa.allinapp.features.reports.userstatus.UserStatusFragment
import com.tawa.allinapp.features.routes.RoutesFragment
import com.tawa.allinapp.features.splash.SplashActivity
import com.tawa.allinapp.features.splash.SplashFragment
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
    fun inject(informsFragment: PDVFragment)
    fun inject(calendarFragment: CalendarFragment)
    fun inject(reportsFragment: ReportsFragment)
    fun inject(loginFragment: LoginFragment)
    fun inject(initFragment: InitFragment)
    fun inject(routesFragment: RoutesFragment)
    fun inject(splashFragment: SplashFragment)
    fun inject(splashActivity: SplashActivity)
    fun inject(selectorDialogFragment: SelectorDialogFragment)
    fun inject(checkInSelectorDialogFragment: CheckInDialogFragment)
    fun inject(checkListFragment: CheckListFragment)
    fun inject(pictureFragment: PictureFragment)
    fun inject(userStatusFragment: UserStatusFragment)
    fun inject(skuFragment: SkuFragment)
    fun inject(lastLocationFragment: LastLocationFragment)
    fun inject(audioFragment: AudioFragment)
}