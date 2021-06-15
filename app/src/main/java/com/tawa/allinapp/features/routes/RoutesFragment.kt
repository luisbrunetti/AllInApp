package com.tawa.allinapp.features.routes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tawa.allinapp.core.di.viewmodel.ViewModelFactory
import com.tawa.allinapp.core.extensions.observe
import com.tawa.allinapp.core.extensions.viewModel
import com.tawa.allinapp.core.platform.BaseFragment

import com.tawa.allinapp.databinding.FragmentInitBinding
import com.tawa.allinapp.databinding.FragmentRoutesBinding


class RoutesFragment : BaseFragment() {

    private lateinit var routesViewModel: RoutesViewModel
    private lateinit var binding: FragmentRoutesBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentRoutesBinding.inflate(inflater)

        routesViewModel = viewModel(viewModelFactory) {
            observe(text, {
                it?.let {
                    binding.tvRoutes.text = it
                }
            })
        }

        return binding.root
    }

}