package com.tawa.allinapp.core.di

import com.tawa.allinapp.AndroidApplication
import com.tawa.allinapp.core.di.viewmodel.ViewModelModule
import com.tawa.allinapp.features.auth.ui.LoginFragment
import com.tawa.allinapp.features.HomeActivity
import com.tawa.allinapp.features.auth.ui.LoginActivity
import com.tawa.allinapp.features.calendar.CalendarFragment
import com.tawa.allinapp.features.coverage.ui.CoverageBoardFragment
import com.tawa.allinapp.features.coverage.ui.CoverageBoardGraphFragment
import com.tawa.allinapp.features.informs.InformsFragment
import com.tawa.allinapp.features.init.ui.*


import com.tawa.allinapp.features.location.LocationFragment
import com.tawa.allinapp.features.movies.ui.MoviesFragment
import com.tawa.allinapp.features.pdv.PdvFragment
import com.tawa.allinapp.features.pdv.TakePhotoDialogFragment
import com.tawa.allinapp.features.reports.picture.PictureFragment
import com.tawa.allinapp.features.reports.standard.CheckListFragment
import com.tawa.allinapp.features.reports.ReportsFragment
import com.tawa.allinapp.features.reports.geolocation.ui.ReportGeolocationFragment
import com.tawa.allinapp.features.reports.sku.SkuFragment
import com.tawa.allinapp.features.reports.userstatus.LastLocationFragment
import com.tawa.allinapp.features.reports.userstatus.UserStatusFragment
import com.tawa.allinapp.features.routes.RoutesFragment
import com.tawa.allinapp.features.splash.SplashActivity
import com.tawa.allinapp.features.splash.SplashFragment
import com.tawa.allinapp.features.task.ui.TaskFragment
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
    fun inject(informsFragment: InformsFragment)
    fun inject(sendPasswordFragment: SendPasswordFragment)
    fun inject(coverageBoardFragment: CoverageBoardFragment)
    fun inject(coverageBoardGraphFragment: CoverageBoardGraphFragment)
    fun inject(pdvFragment: PdvFragment)
    fun inject(takePhotoDialogFragment: TakePhotoDialogFragment)
    fun inject(reportGeolocationFragment: ReportGeolocationFragment)
    fun inject(messagesDialogFragment: MessagesDialogFragment)
    fun inject(taskFragment: TaskFragment)
    //fun inject(MessagesFragment: MessagesFragment)
    //fun inject(confirmNewGroupFragment: ConfirmNewGroupFragment)
}