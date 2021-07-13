package com.tawa.allinapp.features.reports.userstatus

import android.app.DatePickerDialog
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import androidx.navigation.fragment.findNavController
import com.tawa.allinapp.R
import com.tawa.allinapp.core.extensions.observe
import com.tawa.allinapp.core.extensions.viewModel
import com.tawa.allinapp.core.platform.BaseFragment
import com.tawa.allinapp.databinding.FragmentUserStatusBinding
import com.tawa.allinapp.features.init.ui.SelectorDialogFragment
import com.tawa.allinapp.features.reports.reports.ReportsFragmentDirections
import com.tawa.allinapp.models.ReportStatus
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*
import kotlin.collections.ArrayList


class UserStatusFragment : BaseFragment() {

    private lateinit var userStatusViewModel: UserStatusViewModel
    private lateinit var binding: FragmentUserStatusBinding
    private var count = 0
    val formatter = SimpleDateFormat("HH:mm:ss")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentUserStatusBinding.inflate(inflater)
        val userStatus = ArrayList<String>()
        val batteryStatus = ArrayList<Int>()
        val state = ArrayList<String>()
        val stateGps = ArrayList<String>()
        val lastHour = ArrayList<String>()
        val lastPosition  = ArrayList<ArrayList<String>>()
        lateinit var listStatus:List<ReportStatus>


        userStatusViewModel = viewModel(viewModelFactory) {
            observe(reportStatus, { it?.let {
                    listStatus = it
                    if(order.value==0)
                    {
                        userStatus.removeAll(userStatus)
                        batteryStatus.removeAll(batteryStatus)
                        state.removeAll(state)
                        stateGps.removeAll(stateGps)
                        lastHour.removeAll(lastHour)
                        lastPosition.removeAll(lastPosition)

                        for(report in it)
                    {
                        userStatus.add(report.name)
                        batteryStatus.add(report.battery)
                        if(report.status=="ACT")
                            state.add("Activo")
                        else
                            state.add("Inactivo")
                        stateGps.add("Encendido")
                        lastHour.add(getDate(report.lastConnection))
                        val  coordinate  = ArrayList<String>(3)
                        coordinate.add(report.lastLatitude.toString())
                        coordinate.add(report.lastLongitude.toString())
                        coordinate.add(report.name)
                        lastPosition.add(coordinate)
                    }
                    setDataLayout(binding.tlName,userStatus,order.value!!)
                    setDataStatus(binding.tlGridTable,batteryStatus,state,stateGps,lastHour,lastPosition,order.value!!)
                    }
            }
            })
        }

        binding.etDateUserStatus.setOnClickListener{
            getDay(binding.etDateUserStatus)
        }
        binding.btnBackStatus.setOnClickListener{
            activity?.onBackPressed()
        }


        binding.tvBattery.setOnClickListener{

            if(count%2==0) {
                binding.tvBattery.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_arrow_bottom_status,
                    0
                )
                userStatus.removeAll(userStatus)
                batteryStatus.removeAll(batteryStatus)
                state.removeAll(state)
                stateGps.removeAll(stateGps)
                lastHour.removeAll(lastHour)
                lastPosition.removeAll(lastPosition)
                val listAsc = listStatus.sortedBy { it.battery }
                for(report in listAsc)
                {
                    userStatus.add(report.name)
                    batteryStatus.add(report.battery)
                    if(report.status=="ACT")
                        state.add("Activo")
                    else
                        state.add("Inactivo")
                    stateGps.add("Encendido")
                    lastHour.add(getDate(report.lastConnection))
                    val  coordinate  = ArrayList<String>(3)
                    coordinate.add(report.lastLatitude.toString())
                    coordinate.add(report.lastLongitude.toString())
                    coordinate.add(report.name)
                    lastPosition.add(coordinate)
                }
                setDataLayout(binding.tlName,userStatus,1)
                setDataStatus(binding.tlGridTable,batteryStatus,state,stateGps,lastHour,lastPosition,1)
            }
            else {
                binding.tvBattery.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_arrow_top_status,
                    0
                )
                userStatus.removeAll(userStatus)
                batteryStatus.removeAll(batteryStatus)
                state.removeAll(state)
                stateGps.removeAll(stateGps)
                lastHour.removeAll(lastHour)
                lastPosition.removeAll(lastPosition)
                val listDesc = listStatus.sortedByDescending { it.battery }
                for(report in listDesc)
                {
                    userStatus.add(report.name)
                    batteryStatus.add(report.battery)
                    if(report.status=="ACT")
                        state.add("Activo")
                    else
                        state.add("Inactivo")
                    stateGps.add("Encendido")
                    lastHour.add(getDate(report.lastConnection))
                    val  coordinate  = ArrayList<String>(3)
                    coordinate.add(report.lastLatitude.toString())
                    coordinate.add(report.lastLongitude.toString())
                    coordinate.add(report.name)
                    lastPosition.add(coordinate)
                }
                setDataLayout(binding.tlName,userStatus,2)
                setDataStatus(binding.tlGridTable,batteryStatus,state,stateGps,lastHour,lastPosition,2)
            }
            count++
        }
        return binding.root
    }

    private fun setDataLayout(tableLayout: TableLayout, data:ArrayList<String>,type:Int){
        if(type>0)
            tableLayout.removeViews(1, data.size)

        for((flag, name) in data.withIndex())
        {
            val row  = TableRow(context)
            row.setBackgroundResource(R.drawable.borderbottom)
            val textView = TextView(context)
            textView.text = name
            textView.textSize =12f
            textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorTextAll))
            textView.setTypeface(textView.typeface,Typeface.BOLD)
            textView.height=60f.toDips().toInt()
            textView.width = 150f.toDips().toInt()
            textView.setPadding(5)
            textView.gravity = Gravity.CENTER
            row.addView(textView)
            tableLayout.addView(row)
        }
    }

    private fun setDataStatus(tableLayout: TableLayout, battery:ArrayList<Int>,state : ArrayList<String>,stateGps:ArrayList<String>,lastHour:ArrayList<String>,lastPosition:ArrayList<ArrayList<String>>,type:Int){
        if(type>0)
            tableLayout.removeViews(1,battery.size)
        val tvLastPosition = ArrayList<ImageView>()
        for((flag, percent) in battery.withIndex())
        {
            val row  = TableRow(context)
            row.setBackgroundResource(R.drawable.borderbottomtlcontent)
            val  tvBattery = TextView(context)
            textViewStyle(tvBattery,"$percent %")
            row.addView(tvBattery)

            val  tvState = TextView(context)
            textViewStyle(tvState,state[flag])
            row.addView(tvState)

            val  tvStateGps = TextView(context)
            textViewStyle(tvStateGps,stateGps[flag])
            row.addView(tvStateGps)

            val  tvLastHour = TextView(context)
            textViewStyle(tvLastHour,lastHour[flag])
            row.addView(tvLastHour)

            val imageView = ImageView(context)
            tvLastPosition.add(imageView)
            tvLastPosition[flag].setImageResource (R.drawable.ic_show_map)
            val params = TableRow.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                weight = 1.0f
                gravity = Gravity.CENTER
            }
            tvLastPosition[flag].layoutParams = params
            tvLastPosition[flag].tag = lastPosition[flag]
            tvLastPosition[flag].setOnClickListener{
                val tag:ArrayList<String> = tvLastPosition[flag].tag as java.util.ArrayList<String>
                findNavController().navigate(UserStatusFragmentDirections.actionUserStatusFragmentToLastLocationFragment(tag[0],tag[1],tag[2]))
            }

            row.addView(tvLastPosition[flag])

            tableLayout.addView(row)
        }
    }

    private fun setDataStatusOrder(){

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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getDate(text: String):String{
        val instant: Instant = Instant.parse(text)
        val myDate = Date.from(instant)
        return formatter.format(myDate)
    }

    private fun textViewStyle(textView: TextView, text:String){
        textView.text =text
        textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorTextAll))
        textView.height=60f.toDips().toInt()
        textView.setPadding(10)
        textView.gravity = Gravity.CENTER
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