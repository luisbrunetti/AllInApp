package com.tawa.allinapp.features.reports.sku

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tawa.allinapp.R
import com.tawa.allinapp.databinding.FragmentSkuBinding


class SkuFragment : Fragment() {

    private lateinit var  binding: FragmentSkuBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSkuBinding.inflate(inflater)
        return binding.root
    }

}