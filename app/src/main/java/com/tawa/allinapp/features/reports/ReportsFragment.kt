package com.tawa.allinapp.features.reports

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tawa.allinapp.core.extensions.observe
import com.tawa.allinapp.core.extensions.viewModel
import com.tawa.allinapp.core.platform.BaseFragment
import com.tawa.allinapp.databinding.FragmentReportsBinding


class ReportsFragment : BaseFragment() {

    private lateinit var reportsViewModel: ReportsViewModel
    private lateinit var binding: FragmentReportsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentReportsBinding.inflate(inflater)
        reportsViewModel = viewModel(viewModelFactory) {
            observe(text, {
                it?.let {
                    binding.tvReports.text = it
                }
            })
        }
        return binding.root
    }
}