package com.tawa.allinapp.features.reports.userstatus

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Layout
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.setPadding
import com.tawa.allinapp.R
import com.tawa.allinapp.core.extensions.observe
import com.tawa.allinapp.core.extensions.viewModel
import com.tawa.allinapp.core.platform.BaseFragment
import com.tawa.allinapp.databinding.FragmentUserStatusBinding
import kotlinx.android.synthetic.main.dialog_home.*
import java.util.*
import kotlin.collections.ArrayList


class UserStatusFragment : BaseFragment() {

    private lateinit var userStatusViewModel: UserStatusViewModel
    private lateinit var binding: FragmentUserStatusBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentUserStatusBinding.inflate(inflater)
        val userStatus = ArrayList<String>()
        userStatus.add("Marcos Gonzáles Cárdenas")
        userStatus.add("Guillem San Martin")
        userStatus.add("Andoni Espejo")
        userStatus.add("Valentin Cantos")
        userStatus.add("Rachid Oliver")


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
        setDataLayout(binding.tlName,userStatus)

        return binding.root
    }

    private fun setDataLayout(tableLayout: TableLayout, data:ArrayList<String>){
        for((flag, name) in data.withIndex())
        {
            val row  = TableRow(context)
            row.setBackgroundResource(R.drawable.borderbottom)
            val textView = TextView(context)
            textView.text = name
            textView.height=60f.toDips().toInt()
            textView.width = 150f.toDips().toInt()
            textView.setPadding(10)
            textView.gravity = Gravity.CENTER
            row.addView(textView)
            tableLayout.addView(row)
        }



    }

    private fun Float.toDips() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, resources.displayMetrics)

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