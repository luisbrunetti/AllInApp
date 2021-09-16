package com.tawa.allinapp.features.reports.geolocation

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.compose.ui.text.toLowerCase
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tawa.allinapp.R
import com.tawa.allinapp.core.dialog.MessageDialogFragment
import com.tawa.allinapp.core.extensions.invisible
import com.tawa.allinapp.core.extensions.observe
import com.tawa.allinapp.core.extensions.viewModel
import com.tawa.allinapp.core.platform.BaseFragment
import com.tawa.allinapp.databinding.FragmentReportGeolocationBinding
import com.tawa.allinapp.features.reports.geolocation.ui.CheckableSpinnerAdapter
import com.tawa.allinapp.features.reports.geolocation.ui.CheckableSpinnerAdapter.SpinnerItem
import com.tawa.allinapp.features.reports.geolocation.ui.RecyclerUser
import com.tawa.allinapp.models.RoutesUser
import com.tawa.allinapp.models.TrackingInform
import okhttp3.Route
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.stream.Collectors
import kotlin.collections.ArrayList


class ReportGeolocationFragment : BaseFragment(){

    private lateinit var binding: FragmentReportGeolocationBinding
    private lateinit var reportGeoViewModel: ReportGeolocationViewModel
    private val spinner_items: ArrayList<SpinnerItem<RoutesUser>> = ArrayList()
    private val selected_items: MutableSet<RoutesUser> = HashSet()
    private var adapterUser : CheckableSpinnerAdapter<RoutesUser>? = null
    private var recyclerAdapter : RecyclerUser? = null
    private var listRecycleView: ArrayList<RoutesUser>? = null
    private var backupRecycleView: ArrayList<RoutesUser> ? = ArrayList()
    private var mDay: Int? = null
    private var mMonth: Int ? = null
    private var mYear: Int? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentReportGeolocationBinding.inflate(inflater)
        reportGeoViewModel = viewModel(viewModelFactory) {
            observe(getUserList, {
                it?.let {
                    //Log.d("asd", it[0].toString())
                    //userAdapter.collection = it
                    showUser(it)
                }
            })
            observe(getRouteUsers, {
                hideProgressDialog()
                it?.let {
                    if (it.isNotEmpty()) {
                        showMapRoutesDialog(it)
                    }
                }
            })
            observe(failure,{
                hideProgressDialog()
                it?.let {
                    val dialog = MessageDialogFragment.newInstance(it.toString())
                    dialog.show(childFragmentManager,"dialog")
                }
            })
        }

        binding.edUserRoutes.setOnClickListener {
            if(binding.cvUsersReportLocation.visibility == View.VISIBLE) binding.cvUsersReportLocation.visibility = View.GONE
            else binding.cvUsersReportLocation.visibility = View.VISIBLE
        }
        //Binding
        binding.edDateUserRoutes.setOnClickListener {
            getCurrentDay(binding.edDateUserRoutes)
        }
        binding.btnSearchGeolocation.setOnClickListener {
            //val value =
            val date = binding.edDateUserRoutes.text.toString()
            recyclerAdapter?.listChecked?.let {
                if (date != "" && it.size > 0) {
                    //val mutableListUser: MutableList<RoutesUser> = it.stream().collect(Collectors.toList()) as MutableList<RoutesUser>
                    //Log.d("mutableList", mutableListUser.toString()) // Modificar tipo a arraylist
                    showProgressDialog()
                    reportGeoViewModel.getRoutesFromListUsers(
                        it, reportGeoViewModel.convertDate(binding.edDateUserRoutes.text.toString())
                    )


                } else {
                    notify(requireActivity(), R.string.warningReportGeolocation)
                }
            }
        }
        val (day,month,year) = reportGeoViewModel.getCurrentDay()
        this.mDay = day
        this.mMonth = month + 1
        this.mYear = year
        val currentDate = SimpleDateFormat("dd/MM/yyyy", Locale("es", "ES")).format(Date())
        binding.clReportGeolocation.setOnClickListener {
            if (binding.cvUsersReportLocation.visibility == View.VISIBLE) binding.cvUsersReportLocation.visibility = View.GONE
        }
        binding.edDateUserRoutes.setText(currentDate)
        binding.edUserRoutes.addTextChangedListener( object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                binding.cvUsersReportLocation.visibility = View.VISIBLE
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if(s.toString() == "") binding.cvUsersReportLocation.invisible()
                filter(s.toString())
            }

        })

        //Llamadas a los servicios
        reportGeoViewModel.getListUser()
        return binding.root
    }

    private fun showUser(list:List<RoutesUser>){
        listRecycleView = ArrayList<RoutesUser>()
        listRecycleView?.let { it->
            for((count, user) in list.withIndex()){
                if(count < 1 ){
                    val route = RoutesUser("1","Selecionar todos")
                    it.add(route)
                    backupRecycleView?.add(route)
                }
                else{
                    it.add(user)
                    backupRecycleView?.add(user)
                }
            }
        } ?: emptyList<RoutesUser>()
        recyclerAdapter = RecyclerUser(listRecycleView!!,requireContext())
        binding.rvUsersReportLocation.layoutManager = LinearLayoutManager(requireContext())
        binding.rvUsersReportLocation.adapter = recyclerAdapter
        //val headerText = "Selecionar usuario"
        //adapterUser = CheckableSpinnerAdapter(requireActivity(),headerText,spinner_items,selected_items)
        //binding.spinner.adapter = adapterUser
    }

    private fun filter(text: String){
        val newList = ArrayList<RoutesUser>()
        backupRecycleView?.let{  orgList ->
            for(user in orgList){
                if(user.name.toLowerCase(Locale.ROOT).contains(text.toLowerCase(Locale.ROOT))){ newList.add(user) }
            }
        }
        recyclerAdapter?.filteredList(newList)
    }

    private fun getCurrentDay(et:EditText){
        //Log.d("log",(mDay.toString()+mMonth.toString()+mYear.toString()).toString())
        mDay?.let { day->
            mMonth?.let { month ->
                mYear?.let { year ->
                    val dpd = DatePickerDialog(requireContext(),{ _, yearDP, monthDP, dayDP ->
                        if(dayDP < 10){
                            val zeroDay = "0$dayDP"
                            et.setText(""+ zeroDay + "/" + getMonth(monthDP)+ "/"+ yearDP)
                        }else{
                            et.setText(""+ dayDP + "/" + getMonth(monthDP)+ "/"+ yearDP)
                        }
                    },year, month-1,day)
                    dpd.show()
                }
            }
        }
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

    private fun showMapRoutesDialog(listRoutes:List<TrackingInform>){
        val dialog = InformRoutesMapDialogFragment.newInstance(listRoutes)
        dialog.show(childFragmentManager,"")
    }

}