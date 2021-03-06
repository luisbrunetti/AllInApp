package com.tawa.allinapp.features.reports.sku

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.setPadding
import androidx.fragment.app.DialogFragment
import com.tawa.allinapp.R
import com.tawa.allinapp.core.extensions.observe
import com.tawa.allinapp.core.extensions.viewModel
import com.tawa.allinapp.core.platform.BaseFragment
import com.tawa.allinapp.databinding.DialogConfirmSkuBinding
import com.tawa.allinapp.databinding.DialogObservationsSkuBinding
import javax.inject.Inject


class ConfirmDialogFragment(val baseFragment: BaseFragment): DialogFragment() {

    private lateinit var binding: DialogConfirmSkuBinding

    var params = LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.FILL_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    )
    var listener: Callback? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DialogConfirmSkuBinding.inflate(inflater)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        isCancelable = false


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnConfirmSaveSku.setOnClickListener {

            listener?.onClickConfirm()
            dismiss()
        }
        binding.btnSkuBack.setOnClickListener {
            listener?.onClickOnBack()
            dismiss()
        }
        changeViewFragment()
    }

    private fun changeViewFragment(){
        baseFragment.translateObject.apply {
            binding.textView14.text = findTranslate("tvTitleConfimSkuDialog") ?: "??Est??s seguro de guardar la informaci??n?"
            binding.textView20.text = findTranslate("tvSubTitleConfimSkuDialog") ?: "Est??s a punto de guardar la informaci??n"
            binding.btnConfirmSaveSku.text = findTranslate("tvButtonSaveConfirmSkuDialog") ?: "Guardar"
            binding.btnSkuBack.text = findTranslate("tvButtonBackConfirmSkuDialog") ?: "Volver"
        }
    }

    override fun onResume() {
        super.onResume()
        val params = dialog!!.window!!.attributes
        params.width = ConstraintLayout.LayoutParams.MATCH_PARENT
        dialog!!.window!!.attributes = params as android.view.WindowManager.LayoutParams
    }

    interface Callback {
        fun onClickConfirm()
        fun onClickOnBack()
    }
}