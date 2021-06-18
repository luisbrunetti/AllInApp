package com.tawa.allinapp.core.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import com.tawa.allinapp.core.extensions.observe
import com.tawa.allinapp.core.extensions.viewModel
import com.tawa.allinapp.core.platform.BaseFragment
import com.tawa.allinapp.databinding.DialogCheckinBinding
import com.tawa.allinapp.databinding.DialogHomeBinding
import com.tawa.allinapp.features.auth.Company
import com.tawa.allinapp.features.auth.PV
import com.tawa.allinapp.features.init.InitViewModel
import javax.inject.Inject


class CheckinSelectorDialogFragment
@Inject constructor(
    private val baseFragment: BaseFragment
): DialogFragment() {

    private lateinit var binding: DialogCheckinBinding
    private  lateinit var initViewModel: InitViewModel
    var listener: Callback? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DialogCheckinBinding.inflate(inflater)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        isCancelable = false
        val arrayListPv:ArrayList<String> = ArrayList<String>()
        val  aaPv = ArrayAdapter<String>(requireActivity(), android.R.layout.simple_spinner_dropdown_item, arrayListPv)
        //val arrayList:ArrayList<String> = ArrayList<String>()

       // val  aa = ArrayAdapter<String>(requireActivity(), android.R.layout.simple_spinner_dropdown_item, arrayList)

        initViewModel = viewModel(baseFragment.viewModelFactory){

            observe(startCheckIn, {
                it?.let {

                    if(it) {
                        //getCompanies()
                        getPv()

                    }

                }
            })


            observe(pv, {
                it?.let {
                    arrayListPv.addAll(toArrayPv(it))
                    binding.pdvSpinner.adapter = aaPv
                }
            })



        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




        /*arguments?.let { bundle ->
            bundle.getInt(TITLE)?.let { binding.tvTitle.text = context?.getString(it) }
            bundle.getString(MESSAGE)?.let { binding.tvMessage.text = it }
            bundle.getInt(BUTTON).let { binding.btAccept.text = context?.getString(it) }
            bundle.getInt(ICON).let {
                if(it == NO_ICON) binding.ivIcon.invisible() else {
                    binding.ivIcon.visible()
                    binding.ivIcon.setImageResource(it)
                }
            }
        }*/

        //binding.spinner.onItemSelectedListener = this


        binding.btnDoCheckin.setOnClickListener {
            listener?.onAccept()
            dismiss()
            Toast.makeText(context,"Guardar checkin",Toast.LENGTH_SHORT).show()
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