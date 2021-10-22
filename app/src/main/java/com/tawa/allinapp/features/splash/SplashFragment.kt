package com.tawa.allinapp.features.splash

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tawa.allinapp.core.extensions.observe
import com.tawa.allinapp.core.extensions.viewModel
import com.tawa.allinapp.core.platform.BaseFragment
import com.tawa.allinapp.databinding.FragmentSplashBinding

class SplashFragment : BaseFragment() {

    private lateinit var splashViewModel: SplashViewModel
    private lateinit var binding: FragmentSplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSplashBinding.inflate(inflater)
        splashViewModel = viewModel(viewModelFactory) {
            observe(session, {
                Log.d("SplashFragment",session.toString())
                when(it){
                    true -> showHome(context,false)
                    else -> showLogin(context)
                }
            })
        }
        splashViewModel.getSession()
        return binding.root
    }

    override fun changeViewsFragment() {}
}