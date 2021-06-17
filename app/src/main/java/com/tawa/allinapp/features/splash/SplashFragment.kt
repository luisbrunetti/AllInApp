package com.tawa.allinapp.features.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tawa.allinapp.core.extensions.observe
import com.tawa.allinapp.core.extensions.viewModel
import com.tawa.allinapp.core.platform.BaseFragment

class SplashFragment : BaseFragment() {

    private lateinit var splashViewModel: SplashViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        splashViewModel = viewModel(viewModelFactory) {
            observe(session, {
                when(it){
                    true -> showHome(context)
                    else -> showLogin(context)
                }
            })
        }
        splashViewModel.getSession()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

}