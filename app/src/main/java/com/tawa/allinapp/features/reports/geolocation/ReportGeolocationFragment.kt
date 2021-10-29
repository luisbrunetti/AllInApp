package com.tawa.allinapp.features.reports.geolocation

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import com.tawa.allinapp.R
import com.tawa.allinapp.core.dialog.MessageDialogFragment
import com.tawa.allinapp.core.extensions.observe
import com.tawa.allinapp.core.extensions.viewModel
import com.tawa.allinapp.core.platform.BaseFragment
import com.tawa.allinapp.databinding.FragmentReportGeolocationBinding
import com.tawa.allinapp.features.init.InitViewModel
import com.tawa.allinapp.features.reports.geolocation.ui.RecyclerUser
import com.tawa.allinapp.models.RoutesUser
import com.tawa.allinapp.models.TrackingInform
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class ReportGeolocationFragment : BaseFragment(), RecyclerUser.onClickButton{

    private lateinit var binding: FragmentReportGeolocationBinding
    private lateinit var reportGeoViewModel: ReportGeolocationViewModel
    private lateinit var initViewModel: InitViewModel
    private var recyclerAdapter : RecyclerUser? = null
    private var listRecycleView: ArrayList<RoutesUser>? = null
    private var backupRecycleView: ArrayList<RoutesUser> ? = ArrayList()

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
                    Log.d("asd", it.toString())
                    //userAdapter.collection = it
                    showUser(it)
                }
            })
            observe(getRouteUsers, {
                hideProgressDialog()
                it?.let {
                    Log.d("tracking info", it.toString())
                    var fullEmpty = true
                    for(tracking in it){
                        if(tracking.listTracking.isEmpty()){
                            fullEmpty = true
                        }else{
                            fullEmpty = false
                            break
                        }
                    }
                    if (!fullEmpty) showMapRoutesDialog(it)
                    else notify(requireActivity() ,R.string.no_data_tracking)
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
        binding.edUserGeoLocation.setOnClickListener {
            if(binding.cvUsersReportLocation.visibility == View.VISIBLE) binding.cvUsersReportLocation.visibility = View.GONE
            else binding.cvUsersReportLocation.visibility = View.VISIBLE
        }
        //Binding
        binding.edDateGeoLocation.setOnClickListener {
            openDatePicker(binding.edDateGeoLocation)
        }
        binding.btnSearchGeoLocation.setOnClickListener {
            val date = binding.btnSearchGeoLocation.text.toString()
            recyclerAdapter?.listChecked?.let {
                if (date != "" && it.size > 0) {
                    showProgressDialog()
                    reportGeoViewModel.getRoutesFromListUsers(
                        it, reportGeoViewModel.convertDate(binding.edDateGeoLocation.text.toString())
                    )
                } else {
                    notify(requireActivity(), R.string.warningReportGeolocation)
                }
            }
        }

        //Obteniendo el dia en hoy en formato lat
        updateCurrentDate()

        binding.clReportGeolocation.setOnClickListener {
            if (binding.cvUsersReportLocation.visibility == View.VISIBLE) binding.cvUsersReportLocation.visibility = View.GONE
            //binding.edUserRoutes.setText("")
        }

        //Seteando la fecha de hoy
        binding.edDateGeoLocation.setText(SimpleDateFormat("dd/MM/yyyy", Locale("es", "ES")).format(Date()))
        /*binding.edUserRoutes.addTextChangedListener( object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                binding.cvUsersReportLocation.visibility = View.VISIBLE
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if(s.toString() == "") binding.cvUsersReportLocation.invisible()
                filter(s.toString())
            }

        })*/
        //Llamadas a los servicios
        reportGeoViewModel.getListUser()

        changeViewsFragment()
        binding.iHeader.tvHeaderSubtitle.text = "ANOTHER BIRCK ON THE WALL "
        return binding.root
    }

    override fun changeViewsFragment() {
        translateObject.apply {
            binding.btnSearchGeoLocation.text = findTranslate("btnSearchGeoLocation")
            //binding.edDateGeoLocation.hint = findTranslate("edDateGeoLocation")
            binding.edUserGeoLocation.hint = findTranslate("edUserGeoLocation")
            binding.tvDateGeoLocation.hint = findTranslate("tvDateGeoLocation")
            binding.tvUserGeoLocation.text = findTranslate("tvUserGeoLocation")
            Log.d("findTranslate",findTranslate("tvReportGeolocation").toString() + findTranslate("tvInformCheckListFragment").toString())
            binding.iHeader.tvHeaderSubtitle.text = findTranslate("tvInformCheckListFragment")
            binding.iHeader.tvHeaderTitle.text = findTranslate("tvReportGeolocation")
        }
    }

    private fun showUser(list:List<RoutesUser>){
        listRecycleView = ArrayList<RoutesUser>()
        listRecycleView?.let { it->
            for((count, user) in list.withIndex()){
                if(count < 1 ){
                    val route = RoutesUser("1","Selecionar todos")
                    it.add(route)
                    it.add(user)
                    backupRecycleView?.add(user)
                }
                else{
                    it.add(user)
                    backupRecycleView?.add(user)
                }
            }
        } ?: emptyList<RoutesUser>()
        recyclerAdapter = RecyclerUser(listRecycleView!!,requireContext(),this)
        binding.rvUsersReportLocation.layoutManager = LinearLayoutManager(requireContext())
        binding.rvUsersReportLocation.adapter = recyclerAdapter
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

    private fun openDatePicker(et:EditText){
        //Log.d("log",(mDay.toString()+mMonth.toString()+mYear.toString()).toString())
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

    private fun showMapRoutesDialog(listRoutes:List<TrackingInform>){
        val dialog = InformRoutesMapDialogFragment.newInstance(listRoutes)
        dialog.show(childFragmentManager,"")
    }

    override fun onClick(selectedAll: Boolean) {
        var namesConcant = ""
        var count = 0
        for (userChecked in recyclerAdapter?.listChecked!!) {
            if(selectedAll){
                namesConcant = "Todos seleccionados"
            }else{
                if(count == 0){
                    namesConcant = "${userChecked.name}"
                    count++
                }else{
                    namesConcant = "$namesConcant, ${userChecked.name}"
                }
            }
        }
        binding.edUserGeoLocation.setText(namesConcant)
    }
}