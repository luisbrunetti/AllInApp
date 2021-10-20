package com.tawa.allinapp.features.task.ui

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.tawa.allinapp.R
import com.tawa.allinapp.core.extensions.observe
import com.tawa.allinapp.core.extensions.viewModel
import com.tawa.allinapp.core.platform.BaseFragment
import com.tawa.allinapp.databinding.FragmentTaskBinding
import com.tawa.allinapp.features.init.InitViewModel


class TaskFragment : BaseFragment() {

    private lateinit var binding: FragmentTaskBinding
    private lateinit var initViewModel: InitViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkPermissions()
        initViewModel = viewModel(viewModelFactory){
            observe(getLanguageSaved,{
                it?.let {
                    if(it != BaseFragment.SPANISH){
                        BaseFragment.CURRENT_LANGUAGE = it
                        initViewModel.getLanguageByXml("fragment_task.xml")
                    }
                }
            })
            observe(getLanguageSuccess,{
                it?.let { list ->
                    if(list.isNotEmpty()){
                        listLanguage = it
                        changeLanguage(binding.root)
                    }
                }
            })
        }
        initViewModel.getLanguage()

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTaskBinding.inflate(inflater)

        binding.edDateTask.setOnClickListener {
            getCurrentDay(binding.edDateTask)
        }

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    private fun getCurrentDay(et: EditText){
        updateCurrentDate()
        mDay?.let { day->
            mMonth?.let { month ->
                mYear?.let { year ->
                    val dpd = DatePickerDialog(requireContext(),{ _, yearDP, monthDP, dayDP ->
                        if(dayDP < 10){
                            val zeroDay = "0$dayDP"
                            et.setText(""+ zeroDay + "/" + getMonthByNumber(monthDP)+ "/"+ yearDP)
                        }else{
                            et.setText(""+ dayDP + "/" + getMonthByNumber(monthDP)+ "/"+ yearDP)
                        }
                    },year, month-1,day)
                    dpd.show()
                }
            }
        }
    }
}