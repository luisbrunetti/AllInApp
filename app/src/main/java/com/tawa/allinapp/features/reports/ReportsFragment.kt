package com.tawa.allinapp.features.reports

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.tawa.allinapp.core.extensions.observe
import com.tawa.allinapp.core.extensions.viewModel
import com.tawa.allinapp.core.platform.BaseFragment
import com.tawa.allinapp.databinding.FragmentReportsBinding
import kotlinx.android.synthetic.main.fragment_reports.*
import java.text.SimpleDateFormat
import java.time.Month
import java.util.*


class ReportsFragment : BaseFragment() {

    private lateinit var reportsViewModel: ReportsViewModel
    private lateinit var binding: FragmentReportsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentReportsBinding.inflate(inflater)
        reportsViewModel = viewModel(viewModelFactory) {
            observe(text, {
                it?.let {

                }
            })
        }

        binding.textDate.setOnClickListener{
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)


            val dpd = DatePickerDialog(requireContext(), DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                binding.textDate.setText("" + dayOfMonth + "/" +getMonth(monthOfYear) + "/" + year) }, year, month, day)
            dpd.show()
        }

        binding.textDate.setOnClickListener{
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)
            val dpd = DatePickerDialog(requireContext(), DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                binding.textDate.setText("" + dayOfMonth + "/" +getMonth(monthOfYear) + "/" + year) }, year, month, day)
            dpd.show()
        }
        return binding.root
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