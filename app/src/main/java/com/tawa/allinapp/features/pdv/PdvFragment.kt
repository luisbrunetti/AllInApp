package com.tawa.allinapp.features.pdv

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tawa.allinapp.core.extensions.observe
import com.tawa.allinapp.core.extensions.viewModel
import com.tawa.allinapp.core.platform.BaseFragment
import com.tawa.allinapp.databinding.FragmentCalendarBinding
import com.tawa.allinapp.databinding.FragmentPdvBinding

class PdvFragment : BaseFragment() {

    private lateinit var pdvViewModel: PdvViewModel
    private lateinit var binding: FragmentPdvBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentPdvBinding.inflate(inflater)
        pdvViewModel = viewModel(viewModelFactory) {
            observe(text, {
                it?.let {
                  
                }
            })
        }
        binding.btnShowModalPhotoPdv.setOnClickListener {
            showDialogTakePhoto()
        }
        return binding.root
    }

    private fun showDialogTakePhoto(){
        val dialog = TakePhotoDialogFragment()
        dialog.show(childFragmentManager,"")
    }
}