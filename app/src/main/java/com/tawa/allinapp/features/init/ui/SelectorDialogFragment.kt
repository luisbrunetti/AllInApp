package com.tawa.allinapp.features.init.ui

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.fragment.app.DialogFragment
import com.tawa.allinapp.core.extensions.observe
import com.tawa.allinapp.core.extensions.viewModel
import com.tawa.allinapp.core.platform.BaseFragment
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DialogHomeBinding.inflate(inflater)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        isCancelable = false
        RequestPermission()
        val arrayList:ArrayList<String> = ArrayList<String>()
        val arrayListPv:ArrayList<String> = ArrayList<String>()
        val  aa = ArrayAdapter<String>(requireActivity(), android.R.layout.simple_spinner_dropdown_item, arrayList)
        val  aaPv = ArrayAdapter<String>(requireActivity(), android.R.layout.simple_spinner_dropdown_item, arrayListPv)
        
        initViewModel = viewModel(baseFragment.viewModelFactory){

            observe(companies, {
                it?.let {
                    if(positionCompany.value==-1) {
                        arrayList.addAll(toArray(it))
                        binding.spinnerCompany.adapter = aa
                    }
                    else
                    {
                        Toast.makeText(context, it[positionCompany.value!!].name,Toast.LENGTH_SHORT).show()
                    }

                }
            })

            observe(pv, {
                it?.let {
                    if(positionPv.value==-1)
                    {
                        arrayListPv.addAll(toArrayPv(it))
                        binding.spinnerPv.adapter = aaPv
                    }
                    else
                    {
                      Toast.makeText(context, it[positionPv.value!!].description,Toast.LENGTH_SHORT).show()
                    }

                }
            })

            observe(positionCompany, {
                it?.let {

                    getCompanies()

                }
            })

            observe(positionPv, {
                it?.let {
                    getPv()
                }
            })
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnAccessHome.setOnClickListener {
            listener?.onAccept()
            val positionCompany  = binding.spinnerCompany.selectedItemPosition
            initViewModel.selectPositionCompany(positionCompany)
            val positionPv  = binding.spinnerPv.selectedItemPosition
            initViewModel.selectPositionPv(positionPv)

            dismiss()
        }
    }

    private fun toArray(list : List<Company>):ArrayList<String>{
        val arrayList = ArrayList<String>()
        for(element in list)
        {
            arrayList.add(element.description)

        }
        return  arrayList
    }

    private fun toArrayPv(list : List<PV>):ArrayList<String>{
        val arrayList = ArrayList<String>()
        for(element in list)
        {
            arrayList.add(element.description)

        }
        return  arrayList
    }

    fun RequestPermission(){
        //this function will allows us to tell the user to requesut the necessary permsiion if they are not garented
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