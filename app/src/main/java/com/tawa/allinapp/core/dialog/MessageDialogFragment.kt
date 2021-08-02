package com.tawa.allinapp.core.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import com.tawa.allinapp.R
import com.tawa.allinapp.core.extensions.invisible
import com.tawa.allinapp.core.extensions.visible
import com.tawa.allinapp.databinding.DialogMessageBinding

class MessageDialogFragment : DialogFragment() {
    private lateinit var binding: DialogMessageBinding
    var listener: Callback? = null

    companion object {
        const val TITLE = "title"
        const val MESSAGE = "message"
        const val BUTTON = "left"
        const val ICON = "icon"
        const val NO_ICON = -1
        fun newInstance(message: String, title: Int = R.string.error_unknown, button: Int = R.string.bt_accept, icon: Int = R.drawable.ic_error): MessageDialogFragment {
            val frag = MessageDialogFragment()
            val bundle = Bundle()
            bundle.putInt(TITLE, title)
            bundle.putString(MESSAGE, message)
            bundle.putInt(BUTTON, button)
            bundle.putInt(ICON, icon)
            frag.arguments = bundle
            return frag
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DialogMessageBinding.inflate(inflater)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        isCancelable = false
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let { bundle ->
            bundle.getInt(TITLE).let { binding.tvTitle.text = context?.getString(it) }
            bundle.getString(MESSAGE)?.let { if (it.isNotEmpty()) binding.tvMessage.text = it else binding.tvMessage.visibility = View.INVISIBLE }
            bundle.getInt(BUTTON).let { binding.btAccept.text = context?.getString(it) }
            bundle.getInt(ICON).let {
                if(it == NO_ICON) binding.ivIcon.invisible() else {
                    binding.ivIcon.visible()
                    binding.ivIcon.setImageResource(it)
                }
            }
        }
        binding.btAccept.setOnClickListener {
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