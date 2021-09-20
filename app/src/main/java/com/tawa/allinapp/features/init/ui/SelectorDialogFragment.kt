package com.tawa.allinapp.features.init.ui

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.fragment.app.DialogFragment
import com.tawa.allinapp.core.extensions.observe
import com.tawa.allinapp.core.extensions.viewModel
import com.tawa.allinapp.core.platform.BaseFragment
import com.tawa.allinapp.databinding.DialogHomeBinding
import com.tawa.allinapp.features.auth.AuthViewModel
import com.tawa.allinapp.features.init.InitViewModel
import com.tawa.allinapp.models.Company
import javax.inject.Inject


class SelectorDialogFragment
@Inject constructor(
    private val baseFragment: BaseFragment
): DialogFragment() {

    private lateinit var binding: DialogHomeBinding
    private lateinit var initViewModel: InitViewModel
    private lateinit var authViewModel: AuthViewModel
    private lateinit var selectedCompany: String

    var listener: Callback? = null
    private lateinit var listCompany:List<Company>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DialogHomeBinding.inflate(inflater)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        isCancelable = false
        requestPermission()
        val arrayList:ArrayList<String> = ArrayList()
        val aa = ArrayAdapter(requireActivity(), android.R.layout.simple_spinner_dropdown_item, arrayList)

        initViewModel = viewModel(baseFragment.viewModelFactory){
            observe(companies, { it?.let {
                if(positionCompany.value==-1) {
                    arrayList.addAll(toArray(it))
                    binding.spinnerCompany.adapter = aa
                    listCompany = it
                }
            } })
            observe(positionCompany, { it?.let {
                getCompanies()
            } })
            observe(setIdCompanySuccess, { it?.let { if (it) {
                authViewModel.getPVRemote()
                baseFragment.hideProgressDialog()
            }
            } })
            observe(successGetReports, { it?.let { if (it)
            {
                initViewModel.listReports(selectedCompany)
            }

            } })
            observe(successListReport,{it?.let {
                for(report in it)
                {
                    initViewModel.getQuestionsRemote(report.id)
                }
                dismiss()

            } })
            //observe(failure,{
              //  it?.let {
                    //val message= MessageDialogFragment.newInstance("Ha ocurrido al contectarse con el servidor \n ERROR : ${it.toString()}")
                    //message.show(childFragmentManager,"")
                    //dismiss()
               // }
            //})
        }
        authViewModel = viewModel(baseFragment.viewModelFactory){
            observe(successGetPV, { it?.let {
                if (it)
                    initViewModel.getReportsRemote(selectedCompany)
            } })
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.spinnerCompany.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {  }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                initViewModel.selectPositionCompany(position)
            }
        }
        binding.btnAccessHome.setOnClickListener {
            baseFragment.showProgressDialog()
            val positionCompany = binding.spinnerCompany.selectedItemPosition
            selectedCompany = listCompany[positionCompany].id
            initViewModel.setIdCompany(selectedCompany,listCompany[positionCompany].image)
            // initViewModel.getAudioRemote()
            initViewModel.getReportsSku(selectedCompany)
            initViewModel.getPdvRemote(selectedCompany)
            listener?.onAccept()
        }
    }

    private fun toArray(list : List<Company>):ArrayList<String>{
        val arrayList = ArrayList<String>()
        for(element in list) {
            arrayList.add(element.name)
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