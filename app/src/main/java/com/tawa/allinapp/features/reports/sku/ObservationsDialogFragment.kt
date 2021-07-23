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
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.setPadding
import androidx.fragment.app.DialogFragment
import com.tawa.allinapp.R
import com.tawa.allinapp.databinding.DialogObservationsSkuBinding


class ObservationsDialogFragment: DialogFragment() {

    private lateinit var binding: DialogObservationsSkuBinding
    private var listObservations = ArrayList<String>()
    private lateinit var idSku:String
    var params = LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.FILL_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    )
    private val listCategory = ArrayList<CheckBox>()
    private var listCategoryFilter = ArrayList<String>()
    var listener: Callback? = null
    var lat = ""
    var long = ""
    var nameGen= ""
    companion object {

        fun newInstance(idSku:String,map: MutableMap<String,ArrayList<String>>): ObservationsDialogFragment {
            val frag = ObservationsDialogFragment()
            val bundle = Bundle()
            for (key in map.keys) {
                bundle.putStringArrayList(key,map[key])
            }
            bundle.putString("data", idSku)
            frag.arguments = bundle
            return frag
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DialogObservationsSkuBinding.inflate(inflater)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        isCancelable = false
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let { bundle ->
            bundle.getString("data")?.let {
                idSku= it
            }
            bundle.getStringArrayList(idSku).let { observations->
                if(!observations.isNullOrEmpty())
                {
                    for(observation in observations)
                    {
                        addObservations(observation,binding.addObservations)
                    }
                }
            }
        }
        binding.btnCloseModalObservations.setOnClickListener {
            dismiss()
        }
        binding.btnSaveObservations.setOnClickListener {
            dismiss()
            listener?.onClick(idSku,binding.edObservation.text.toString())
        }
    }

    private fun addObservations(observations:String,linearContent:LinearLayout){
        val editText = EditText(context)
        editText.setText(observations)
        editText.setBackgroundResource(R.drawable.rounded)
        editText.isEnabled = false
        editText.setPadding(10f.toDips().toInt())
        params.bottomMargin = 15f.toDips().toInt()
        editText.layoutParams = params
        linearContent.addView(editText)
    }
    private fun Float.toDips() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, resources.displayMetrics)

    override fun onResume() {
        super.onResume()
        val params = dialog!!.window!!.attributes
        params.width = ConstraintLayout.LayoutParams.MATCH_PARENT
        dialog!!.window!!.attributes = params as android.view.WindowManager.LayoutParams
    }

    interface Callback {
        fun onClick(id:String,list:String)
    }
}