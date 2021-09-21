package com.tawa.allinapp.features.init.ui

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Location
import android.location.LocationManager
import android.os.BatteryManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.fragment.app.DialogFragment
import com.google.android.gms.location.*
import com.tawa.allinapp.core.extensions.observe
import com.tawa.allinapp.core.extensions.viewModel
import com.tawa.allinapp.core.platform.BaseFragment
import com.tawa.allinapp.databinding.DialogCheckinBinding
import com.tawa.allinapp.databinding.DialogSelectPdvBinding
import com.tawa.allinapp.features.init.InitViewModel
import com.tawa.allinapp.models.Schedule
import java.sql.Timestamp
import java.text.SimpleDateFormat
import javax.inject.Inject
import kotlin.collections.ArrayList


class SelectPdvDialogFragment
@Inject constructor(
    private val baseFragment: BaseFragment
): DialogFragment(){

    private lateinit var binding: DialogSelectPdvBinding
    private  lateinit var initViewModel: InitViewModel

    var idUsers = ""
    var checkState =false
    private lateinit var list: List<Schedule>
    private var _pv: String = ""
    private var _pvId: String = ""
    var listener: Callback? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DialogSelectPdvBinding.inflate(inflater)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        isCancelable = false
        val arrayListPv:ArrayList<String> = ArrayList()
        val aaPv = ArrayAdapter(requireActivity(), android.R.layout.simple_spinner_dropdown_item, arrayListPv)

        initViewModel = viewModel(baseFragment.viewModelFactory){
            observe(schedule, { it?.let {
                arrayListPv.addAll(toArrayPv(it))
                binding.selectPdvSpinner.adapter = aaPv
                list= it
            } })
            observe(successGetCompanyId, { it?.let {
                initViewModel.getPv(it)
            } })
        }
        initViewModel.getIdCompany()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpBinding()
        binding.btnSelectPdv.setOnClickListener {
            val positionPv  = binding.selectPdvSpinner.selectedItemPosition
            listener?.onAccept(list[positionPv].id,list[positionPv].pv,list[positionPv].nameCorp)
            dismiss()
        }
        binding.closeCheckInModal.setOnClickListener{
            dismiss()
        }
    }

    private fun toArrayPv(list : List<Schedule>):ArrayList<String>{
        val arrayList = ArrayList<String>()
        for(element in list) {
            arrayList.add(element.nameCorp)
        }
        return  arrayList
    }

    override fun onResume() {
        super.onResume()
        val params = dialog!!.window!!.attributes
        params.width = ConstraintLayout.LayoutParams.MATCH_PARENT
        dialog!!.window!!.attributes = params as WindowManager.LayoutParams
    }

    private fun setUpBinding() {
        binding.viewModel = initViewModel
        binding.lifecycleOwner = this
        binding.executePendingBindings()
    }

    interface Callback {
        fun onAccept(id:String,pv:String,description:String)
        //fun onSnack(snack:Boolean)
    }
}