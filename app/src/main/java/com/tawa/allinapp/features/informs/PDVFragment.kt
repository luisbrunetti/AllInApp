package com.tawa.allinapp.features.informs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tawa.allinapp.core.extensions.observe
import com.tawa.allinapp.core.extensions.viewModel
import com.tawa.allinapp.core.platform.BaseFragment

import com.tawa.allinapp.databinding.FragmentPdvBinding

class PDVFragment : BaseFragment() {

    private lateinit var informsViewModel: InformsViewModel
    private lateinit var binding: FragmentPdvBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentPdvBinding.inflate(inflater)
        informsViewModel = viewModel(viewModelFactory) {
            observe(text, {
                it?.let {
                    binding.tvInforms.text = it
                }
            })
        }
        return binding.root
    }

}