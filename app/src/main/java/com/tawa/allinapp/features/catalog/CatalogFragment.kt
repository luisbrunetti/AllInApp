package com.tawa.allinapp.features.catalog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tawa.allinapp.core.extensions.observe
import com.tawa.allinapp.core.extensions.viewModel
import com.tawa.allinapp.core.platform.BaseFragment
import com.tawa.allinapp.databinding.FragmentCatalogBinding

class CatalogFragment : BaseFragment() {

    private lateinit var catalogViewModel: CatalogViewModel
    private lateinit var binding: FragmentCatalogBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentCatalogBinding.inflate(inflater)
        catalogViewModel = viewModel(viewModelFactory) {
            observe(text, {
                it?.let {
                    binding.tvCatalog.text = it
                }
            })
        }
        return binding.root
    }

}