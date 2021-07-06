package com.tawa.allinapp.features.reports.userstatus

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tawa.allinapp.core.di.viewmodel.ViewModelFactory
import com.tawa.allinapp.core.extensions.observe
import com.tawa.allinapp.core.extensions.viewModel
import com.tawa.allinapp.core.platform.BaseFragment

import com.tawa.allinapp.databinding.FragmentInitBinding
import com.tawa.allinapp.databinding.FragmentRoutesBinding
import com.tawa.allinapp.databinding.FragmentUserStatusBinding
import java.util.*


class UserStatusFragment : BaseFragment() {

    private lateinit var userStatusViewModel: UserStatusViewModel
    private lateinit var binding: FragmentUserStatusBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentUserStatusBinding.inflate(inflater)

        userStatusViewModel = viewModel(viewModelFactory) {
            observe(text, {
                it?.let {

                }
            })
        }

        binding.etDateUserStatus.setOnClickListener{
            getDay(binding.etDateUserStatus)
        }
        binding.btnBackStatus.setOnClickListener{
            activity?.onBackPressed()
        }
        return binding.root
    }

    private fun getDay(et:EditText){
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val dpd = DatePickerDialog(requireContext(), DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            et.setText("" + dayOfMonth + "/" +getMonth(monthOfYear) + "/" + year) }, year, month, day)
        dpd.show()
    }

    private fun getMonth(monthYear: Int) = when(monthYear){
        0 -> "01"
        1 -> "02"
        2 -> "03"
        3 -> "04"
        4 -> "05"
        5 -> "06"
        6 -> "07"
        7 -> "08"
        8 -> "09"
        9 -> "10"
        10 -> "11"
        11 -> "12"
        else  ->""
    }

}