package com.tawa.allinapp.features.reports.picture

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tawa.allinapp.R
import com.tawa.allinapp.core.extensions.observe
import com.tawa.allinapp.core.extensions.viewModel
import com.tawa.allinapp.core.platform.BaseFragment
import com.tawa.allinapp.databinding.FragmentPictureBinding

class PictureFragment : BaseFragment() {

    private lateinit var binding:FragmentPictureBinding
    private lateinit var pictureViewModel: PictureViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentPictureBinding.inflate(inflater)
        pictureViewModel = viewModel(viewModelFactory){
            observe(text,{ it?.let {
                it
            }})
        }
        return binding.root
    }

}