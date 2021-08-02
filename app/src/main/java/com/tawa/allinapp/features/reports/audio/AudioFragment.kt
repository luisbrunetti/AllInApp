package com.tawa.allinapp.features.reports.audio

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tawa.allinapp.R
import com.tawa.allinapp.core.dialog.MessageDialogFragment
import com.tawa.allinapp.core.extensions.failure
import com.tawa.allinapp.core.extensions.observe
import com.tawa.allinapp.core.extensions.viewModel
import com.tawa.allinapp.core.functional.Failure
import com.tawa.allinapp.core.platform.BaseFragment
import com.tawa.allinapp.databinding.FragmentAudioBinding


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
                Log.w("sdasd",it)
                //TODO save record in adapter and make play in clickListener
            }})
            failure(failure, { it?.let {
                hideProgressDialog()
                when(it){
                    is Failure.NetworkConnection    -> MessageDialogFragment.newInstance(getString(R.string.error_network)).show(childFragmentManager, "dialog")
                    is Failure.ServerError          -> MessageDialogFragment.newInstance(getString(R.string.error_network)).show(childFragmentManager, "dialog")
                    else                            -> MessageDialogFragment.newInstance(getString(R.string.error_unknown)).show(childFragmentManager, "dialog")
                }
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