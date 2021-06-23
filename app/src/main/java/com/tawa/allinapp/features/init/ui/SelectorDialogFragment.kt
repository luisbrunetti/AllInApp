package com.tawa.allinapp.features.init.ui

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.fragment.app.DialogFragment
import com.tawa.allinapp.core.extensions.observe
import com.tawa.allinapp.core.extensions.viewModel
import com.tawa.allinapp.core.platform.BaseFragment
import com.tawa.allinapp.data.local.Prefs
import com.tawa.allinapp.databinding.DialogHomeBinding
import com.tawa.allinapp.models.Company
import com.tawa.allinapp.models.PV
import com.tawa.allinapp.features.init.InitViewModel
import javax.inject.Inject


class SelectorDialogFragment
@Inject constructor(
    private val baseFragment: BaseFragment
): DialogFragment() {

    private lateinit var binding: DialogHomeBinding
    private  lateinit var initViewModel: InitViewModel

    var listener: Callback? = null
    private var idCompany:String = ""
    private var idPV:String = ""
    lateinit var listCompany:List<Company>
    lateinit var listPV:List<PV>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DialogHomeBinding.inflate(inflater)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        isCancelable = false
        requestPermission()
        val arrayList:ArrayList<String> = ArrayList<String>()
        val arrayListPv:ArrayList<String> = ArrayList<String>()
        val  aa = ArrayAdapter<String>(requireActivity(), android.R.layout.simple_spinner_dropdown_item, arrayList)
        val  aaPv = ArrayAdapter<String>(requireActivity(), android.R.layout.simple_spinner_dropdown_item, arrayListPv)

        initViewModel = viewModel(baseFragment.viewModelFactory){
            observe(companies, { it?.let {
                if(positionCompany.value==-1) {
                    arrayList.addAll(toArray(it))
                    binding.spinnerCompany.adapter = aa
                    listCompany = it
                }
                else {
                    arrayListPv.removeAll(arrayListPv)
                    getPv(it[positionCompany.value!!].id)
                    idCompany = it[positionCompany.value!!].id
                }
            } })
            observe(pv, { it?.let {
                arrayListPv.addAll(toArrayPv(it))
                binding.spinnerPv.adapter = aaPv
                listPV = it
            } })
            observe(positionCompany, { it?.let {
                getCompanies()
            } })
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.spinnerCompany?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {  }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                initViewModel.selectPositionCompany(position)
            }
        }

        binding.btnAccessHome.setOnClickListener {
            listener?.onAccept()
            val positionPv = binding.spinnerPv.selectedItemPosition
            val positionCompany = binding.spinnerCompany.selectedItemPosition
            if(positionPv>-1) {
                initViewModel.setIdCompany(listCompany[positionCompany].id)
                dismiss()
            }
            else {
                Toast.makeText(context,"Debe seleccionar un punto de venta",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun toArray(list : List<Company>):ArrayList<String>{
        val arrayList = ArrayList<String>()
        for(element in list) {
            arrayList.add(element.name)
        }
        return  arrayList
    }

    private fun toArrayPv(list : List<PV>):ArrayList<String>{
        val arrayList = ArrayList<String>()
        for(element in list) {
            arrayList.add(element.description)
        }
        return  arrayList
    }

    private fun requestPermission(){
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION,android.Manifest.permission.ACCESS_FINE_LOCATION), 1010
        )
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