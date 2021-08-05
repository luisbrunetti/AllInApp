package com.tawa.allinapp.features.informs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.tawa.allinapp.core.extensions.observe
import com.tawa.allinapp.core.extensions.viewModel
import com.tawa.allinapp.core.platform.BaseFragment
import com.tawa.allinapp.databinding.FragmentInformsBinding

import com.tawa.allinapp.databinding.FragmentPdvBinding
import com.tawa.allinapp.features.reports.ReportsFragmentDirections

class InformsFragment : BaseFragment() {

    private lateinit var informsViewModel: InformsViewModel
    private lateinit var binding: FragmentInformsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentInformsBinding.inflate(inflater)
        informsViewModel = viewModel(viewModelFactory) {
            observe(text, {
                it?.let {
                 //   binding.tvInforms.text = it
                }
            })
        }
        binding.btnBackInforms.setOnClickListener {
            activity?.onBackPressed()
        }
        binding.btnStatusUser.setOnClickListener {
            findNavController().navigate(InformsFragmentDirections.actionNavigationInformsToUserStatusFragment())
        }
        return binding.root
    }

}