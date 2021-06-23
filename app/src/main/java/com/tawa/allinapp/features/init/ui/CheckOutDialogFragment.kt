package com.tawa.allinapp.features.init.ui

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import com.tawa.allinapp.core.dialog.MessageDialogFragment
import com.tawa.allinapp.core.extensions.invisible
import com.tawa.allinapp.core.extensions.visible
import com.tawa.allinapp.databinding.FragmentCheckOutDialogBinding


class CheckOutDialogFragment : DialogFragment() {
    private lateinit var binding: FragmentCheckOutDialogBinding
    var listener: Callback? = null

    companion object {
        const val PV = "pv"
        fun newInstance(pv: String = ""): CheckOutDialogFragment {
            val frag = CheckOutDialogFragment()
            val bundle = Bundle()
            bundle.putString(PV, pv)
            frag.arguments = bundle
            return frag
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCheckOutDialogBinding.inflate(inflater)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        isCancelable = false
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let { bundle ->
            bundle.getString(PV)?.let { binding.tvPV.text = it }
        }
        binding.btnDoCheckOut.setOnClickListener {
            listener?.onAccept()
            dismiss()
        }
    }

    override fun onResume() {
        super.onResume()
        val params = dialog!!.window!!.attributes
        params.width = ConstraintLayout.LayoutParams.MATCH_PARENT
        dialog!!.window!!.attributes = params as android.view.WindowManager.LayoutParams
    }

    interface Callback {
        fun onAccept()
    }
}