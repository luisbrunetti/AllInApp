package com.tawa.allinapp.features.init

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.tawa.allinapp.R
import com.tawa.allinapp.core.extensions.observe
import com.tawa.allinapp.core.extensions.viewModel
import com.tawa.allinapp.core.platform.BaseFragment

import com.tawa.allinapp.databinding.FragmentInitBinding

class InitFragment : BaseFragment() {

    private lateinit var initViewModel: InitViewModel
    private lateinit var binding: FragmentInitBinding
    var flag= true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentInitBinding.inflate(inflater)
        onCreateDialog().show()
        initViewModel = viewModel(viewModelFactory) {
            observe(text, {
                it?.let {
                    binding.tvInit.text = it
                }
            })
        }




        return binding.root
    }


     private fun onCreateDialog(): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater

            builder.setView(inflater.inflate(R.layout.dialog_home,null))

                .setPositiveButton("Aceptar",
                    DialogInterface.OnClickListener { dialog, id ->


                        Toast.makeText(context,"ACEPTAR",Toast.LENGTH_SHORT).show()


                    })
                .setNegativeButton("Cancelar",
                    DialogInterface.OnClickListener { dialog, id ->
                        Toast.makeText(context,"Cancelar",Toast.LENGTH_SHORT).show()

                    })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

}