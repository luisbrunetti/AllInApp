package com.tawa.allinapp.features.reports.audio

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaRecorder
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.tawa.allinapp.R
import com.tawa.allinapp.core.extensions.observe
import com.tawa.allinapp.core.extensions.viewModel
import com.tawa.allinapp.core.platform.BaseFragment
import com.tawa.allinapp.databinding.FragmentAudioBinding
import com.tawa.allinapp.features.HomeActivity
import java.io.File
import java.io.IOException
import java.util.*


class AudioFragment : BaseFragment() {

    private lateinit var binding: FragmentAudioBinding
    private lateinit var audioViewModel: AudioViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpBinding()
        checkPermissions()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAudioBinding.inflate(inflater)
        audioViewModel = viewModel(viewModelFactory){
            observe(record,{ it?.let {
                //TODO save record in adapter and make play in clickListener
            }})
        }
        return binding.root
    }

    private fun setUpBinding() {
        binding.viewModel = audioViewModel
        binding.lifecycleOwner = this
        binding.executePendingBindings()
    }
}