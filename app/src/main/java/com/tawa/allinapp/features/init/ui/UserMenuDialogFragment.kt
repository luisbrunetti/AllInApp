package com.tawa.allinapp.features.init.ui

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tawa.allinapp.R
import com.tawa.allinapp.core.platform.BaseFragment
import com.tawa.allinapp.core.platform.TranslateObject
import com.tawa.allinapp.databinding.DialogUserMenuBinding
import javax.inject.Inject


class UserMenuDialogFragment
@Inject constructor(val baseFragment: BaseFragment): BottomSheetDialogFragment() {
    private lateinit var binding: DialogUserMenuBinding
    var listener: Callback? = null

    companion object {
        const val BUTTON = "left"
        fun newInstance(baseFragment: BaseFragment): UserMenuDialogFragment {
            val frag = UserMenuDialogFragment(baseFragment)
            val bundle = Bundle()
            frag.arguments = bundle
            return frag
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DialogUserMenuBinding.inflate(inflater)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        isCancelable = true
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let { bundle ->
            //bundle.getInt(TITLE).let { binding.tvItem.text = context?.getString(it) }
        }
        binding.tvLogoutUserMenu.setOnClickListener {
            listener?.onAccept()
            dismiss()
        }
        binding.tvNotifyUserMenu.setOnClickListener {
            listener?.onNotify()
            dismiss()

        }
        changeViewFragment()
    }
    private fun changeViewFragment(){
        baseFragment.translateObject.apply {
            binding.tvLogoutUserMenu.text = findTranslate("tvLogoutUserMenu") ?: "Cerrar sesión"
            binding.tvNotifyUserMenu.text = findTranslate("tvNotifyUserMenu") ?: "Notificaciones"
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
        fun onNotify()
    }
}