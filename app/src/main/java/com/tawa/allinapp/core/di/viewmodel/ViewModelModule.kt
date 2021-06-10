package com.tawa.allinapp.core.di.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tawa.allinapp.features.calendar.CalendarViewModel
import com.tawa.allinapp.features.catalog.CatalogViewModel
import com.tawa.allinapp.features.movies.MoviesViewModel
import com.tawa.allinapp.features.reports.ReportsViewModel
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
    @ViewModelKey(CatalogViewModel::class)
    abstract fun bindsCatalogViewModel(catalogViewModel: CatalogViewModel): ViewModel


}