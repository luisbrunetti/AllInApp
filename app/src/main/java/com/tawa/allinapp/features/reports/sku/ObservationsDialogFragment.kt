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
import com.tawa.allinapp.databinding.DialogObservationsSkuBinding
import javax.inject.Inject


class ObservationsDialogFragment
@Inject constructor(
    private val baseFragment: BaseFragment
): DialogFragment() {

    private lateinit var binding: DialogObservationsSkuBinding
    private lateinit var skuViewModel: SkuViewModel
    private var listObservations = ArrayList<String>()
    private lateinit var idSku:String
    private lateinit var idPv:String
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

        fun newInstance(idSku:String,idPv:String,baseFragment: BaseFragment): ObservationsDialogFragment {
            val frag = ObservationsDialogFragment(baseFragment)
            val bundle = Bundle()
            bundle.putString("data", idSku)
            bundle.putString("idPv", idPv)
            frag.arguments = bundle
            return frag
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DialogObservationsSkuBinding.inflate(inflater)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        isCancelable = false
        skuViewModel = viewModel(baseFragment.viewModelFactory) {
            observe(successGetSkuObservation, {
                    it?.let {
                        if(type.value==1)
                        {
                            if(it.isNotEmpty())
                            {
                                for(observation in it)
                                {
                                    addObservations(observation.observation,binding.addObservations)
                                }
                            }
                        }
                        if(type.value==2)
                        {
                            val count = it.size
                            listener?.onClick(idSku,count)
                            dismiss()
                        }

                    }
            })
            observe(successSetSkuObservation,{
                it?.let {
                    if (it)
                        skuViewModel.getSkuObservation(idSku,2,idPv)
                }

            })


        }
        changeViewFragment()

        return binding.root
    }

    private fun changeViewFragment(){
        baseFragment.translateObject.apply {
            binding.btnSaveObservations.text =  findTranslate("btnSaveObservationDialog")
            binding.textView27.text = findTranslate("btnTitleObservationDialog")
            binding.edObservation.hint = findTranslate("edWriteObservationDialog")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let { bundle ->
            bundle.getString("data")?.let { idSku->
                this.idSku = idSku
                bundle.getString("idPv")?.let { idPv->
                    this.idPv = idPv
                    skuViewModel.getSkuObservation(idSku,1,idPv)
                }
            }
        }
        binding.btnCloseModalObservations.setOnClickListener {
            dismiss()
        }
        binding.btnSaveObservations.setOnClickListener {
            if(binding.edObservation.text.toString().isNotEmpty())
                skuViewModel.setSkuObservation(idSku,idPv,binding.edObservation.text.toString())
            else
                Toast.makeText(context,"Debe ingresar un valor",Toast.LENGTH_SHORT).show()
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
        fun onClick(id:String,count:Int)
    }
}