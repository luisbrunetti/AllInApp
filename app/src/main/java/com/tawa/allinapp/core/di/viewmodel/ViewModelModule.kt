package com.tawa.allinapp.core.di.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tawa.allinapp.features.auth.AuthViewModel
import com.tawa.allinapp.features.calendar.CalendarViewModel
import com.tawa.allinapp.features.coverage.CoverageViewModel
import com.tawa.allinapp.features.informs.InformsViewModel
import com.tawa.allinapp.features.init.InitViewModel
import com.tawa.allinapp.features.movies.MoviesViewModel
import com.tawa.allinapp.features.pdv.PdvViewModel
import com.tawa.allinapp.features.reports.audio.AudioViewModel
import com.tawa.allinapp.features.reports.picture.PictureViewModel
import com.tawa.allinapp.features.reports.standard.CheckListViewModel
import com.tawa.allinapp.features.reports.ReportsViewModel
import com.tawa.allinapp.features.reports.geolocation.ReportGeolocationViewModel
import com.tawa.allinapp.features.reports.sku.SkuViewModel
import com.tawa.allinapp.features.reports.userstatus.UserStatusViewModel
import com.tawa.allinapp.features.routes.RoutesViewModel
import com.tawa.allinapp.features.splash.SplashViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {
    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(MoviesViewModel::class)
    abstract fun bindsMoviesViewModel(moviesViewModel: MoviesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CalendarViewModel::class)
    abstract fun bindsCalendarViewModel(calendarViewModel: CalendarViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ReportsViewModel::class)
    abstract fun bindsReportsViewModel(reportsViewModel: ReportsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(InformsViewModel::class)
    abstract fun bindsInformsViewModel(informsViewModel: InformsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AuthViewModel::class)
    abstract fun bindsAuthViewModel(authViewModel: AuthViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(InitViewModel::class)
    abstract fun bindsInitViewModel(initViewModel: InitViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RoutesViewModel::class)
    abstract fun bindsRoutesViewModel(routesViewModel: RoutesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SplashViewModel::class)
    abstract fun bindsSplashViewModel(splashViewModel: SplashViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CheckListViewModel::class)
    abstract fun bindsCheckListViewModel(checkListViewModel: CheckListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PictureViewModel::class)
    abstract fun bindsPictureViewModel(pictureViewModel: PictureViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(UserStatusViewModel::class)
    abstract fun bindsUserStatusViewModel(userStatusViewModel: UserStatusViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AudioViewModel::class)
    abstract fun bindsAudioViewModel(audioViewModel: AudioViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SkuViewModel::class)
    abstract fun bindsSkuViewModel(skuViewModel: SkuViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CoverageViewModel::class)
    abstract fun bindsCoverageViewModel(coverageViewModel: CoverageViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PdvViewModel::class)
    abstract fun bindsPdvViewModel(pdvViewModel: PdvViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ReportGeolocationViewModel::class)
    abstract fun bindsReportGeolocationViewModel(reportGeolocationViewModel: ReportGeolocationViewModel) : ViewModel



}