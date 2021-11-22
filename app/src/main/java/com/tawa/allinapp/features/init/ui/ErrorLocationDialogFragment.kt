package com.tawa.allinapp.features.init.ui

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import com.tawa.allinapp.core.platform.BaseFragment
import com.tawa.allinapp.databinding.DialogErrorLocationBinding
import javax.inject.Inject


class ErrorLocationDialogFragment
@Inject constructor(val baseFragment: BaseFragment): DialogFragment(){

    private lateinit var binding: DialogErrorLocationBinding
    var listener: Callback? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DialogErrorLocationBinding.inflate(inflater)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        isCancelable = false
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnCloseLocationError.setOnClickListener {
            listener?.onAccept()
            dismiss()
        }
        changeViewFragment()
    }

    private fun changeViewFragment(){
        baseFragment.translateObject.apply {
            binding.tvTitleRangeDialog.text = findTranslate("tvTitleRangeDialog") ?: "No estás en el rango del PDV"
            binding.tvSubTtitleRangeDialog.text = findTranslate("tvSubTtitleRangeDialog") ?: "El máximo de distancia permitido es de 250 metros del PDV para ingresar el Check-In."
            binding.btnCloseLocationError.text = findTranslate("btnCloseLocationError") ?: "Entendido"
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