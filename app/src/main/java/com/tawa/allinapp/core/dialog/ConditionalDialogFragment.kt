package com.tawa.allinapp.core.dialog

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.graphics.Color
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import com.tawa.allinapp.R
import com.tawa.allinapp.databinding.FragmentConditionalDialogBinding


class ConditionalDialogFragment : DialogFragment() {
   private var _binding: FragmentConditionalDialogBinding? = null
   private val binding get() = _binding!!
    var listener: Callback? = null
    companion object {
        const val MESSAGE: String = "message"
        const val TITLE : String = "title"
        const val BTN1: String = "btn1"
        const val BTN2: String = "btn2"
        const val ICON : String = "icon"
        fun newInstance(message: String, title:String,
                        btn1: String, btn2: String, icon: Int = R.drawable.ic_warning) : ConditionalDialogFragment {
            val frag = ConditionalDialogFragment()
            val bundle = Bundle()
            bundle.putString(MESSAGE, message)
            bundle.putString(TITLE, title)
            bundle.putString(BTN1, btn1)
            bundle.putString(BTN2, btn2)
            bundle.putInt(ICON, icon)
            frag.arguments = bundle
            return frag
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentConditionalDialogBinding.inflate(inflater,container,false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
        isCancelable = false
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            it.getString(TITLE).let {  title ->
                binding.tvTitleConditional.text = title
            }
            it.getString(MESSAGE).let { message ->
                binding.tvMessageConditional.text = message
            }
            it.getString(BTN1).let { option ->
                binding.btnOptionOne.text = option
            }
            it.getString(BTN2).let { option ->
                binding.btnOptionTwo.text = option
            }
            it.getInt(ICON).let { iv ->
                binding.ivConditional.setImageResource(iv)
            }
        }
        binding.btnOptionOne.setOnClickListener {
            listener?.onAccept()
            dismiss()
        }
        binding.btnOptionTwo.setOnClickListener { dismiss() }
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