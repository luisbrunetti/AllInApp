package com.tawa.allinapp.core.di

import com.tawa.allinapp.AndroidApplication
import com.tawa.allinapp.core.di.viewmodel.ViewModelModule
import com.tawa.allinapp.features.auth.ui.LoginFragment
import com.tawa.allinapp.features.movies.ui.MoviesFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class, ViewModelModule::class])
interface ApplicationComponent{
    fun inject(application: AndroidApplication)
    fun inject(moviesFragment: MoviesFragment)
    fun inject(loginFragment: LoginFragment)

}