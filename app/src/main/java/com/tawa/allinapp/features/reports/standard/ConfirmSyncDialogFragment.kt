package com.tawa.allinapp.features.reports.standard

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import com.tawa.allinapp.core.platform.BaseFragment
import com.tawa.allinapp.databinding.DialogConfirmSyncBinding
import javax.inject.Inject


class ConfirmSyncDialogFragment
@Inject constructor(
    private val baseFragment: BaseFragment
): DialogFragment() {

    private lateinit var binding: DialogConfirmSyncBinding

    var params = LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.FILL_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    )
    var listener: Callback? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DialogConfirmSyncBinding.inflate(inflater)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        isCancelable = false

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnConfirmSync.setOnClickListener {

            listener?.onClick()
            dismiss()
        }
        binding.btnBackSync.setOnClickListener {
            listener?.onBack()
            dismiss()
        }
        changeViewFragment()
    }

    private fun changeViewFragment() {
        baseFragment.translateObject.apply {
            binding.apply {
                btnBackSync.text = findTranslate("btnBackSync")
                btnConfirmSync.text = findTranslate("btnConfirmSync")
                tvMessageDialogConfirm.text = findTranslate("tvMessageDialogConfirm")
                tvTitleDialogConfirm.text = findTranslate("tvTitleDialogConfirm")
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val params = dialog!!.window!!.attributes
        params.width = ConstraintLayout.LayoutParams.MATCH_PARENT
        dialog!!.window!!.attributes = params as android.view.WindowManager.LayoutParams
    }

    interface Callback {
        fun onClick()
        fun onBack()
    }
}