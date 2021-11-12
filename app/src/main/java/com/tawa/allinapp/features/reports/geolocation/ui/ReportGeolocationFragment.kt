package com.tawa.allinapp.features.reports.geolocation.ui

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import com.tawa.allinapp.core.dialog.MessageDialogFragment
import com.tawa.allinapp.core.extensions.observe
import com.tawa.allinapp.core.extensions.viewModel
import com.tawa.allinapp.core.platform.BaseFragment
import com.tawa.allinapp.databinding.FragmentReportGeolocationBinding
import com.tawa.allinapp.features.reports.geolocation.ReportGeolocationViewModel
import com.tawa.allinapp.models.RoutesUser
import com.tawa.allinapp.models.TrackingInform
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class ReportGeolocationFragment : BaseFragment(), RecyclerUser.onClickButton{

    private lateinit var binding: FragmentReportGeolocationBinding
    private lateinit var reportGeoViewModel: ReportGeolocationViewModel
    private var recyclerAdapter : RecyclerUser? = null
    private var listRecycleView: ArrayList<RoutesUser>? = null
    private var backupRecycleView: ArrayList<RoutesUser> ? = ArrayList()
    private var selectedAll: Boolean = false


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
                        if(tracking.listTracking.isEmpty())fullEmpty = true
                        else{
                            fullEmpty = false
                            break
                        }
                    }
                    if (!fullEmpty) showMapRoutesDialog(it)
                    else notify(requireActivity() ,"No hay datos en esta fecha para los usuarios selecionados")
                }
            })
            observe(failure,{
                hideProgressDialog()
                it?.let {
                    val dialog = MessageDialogFragment.newInstance(this@ReportGeolocationFragment,it.toString())
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
                    val listFiltered =
                        if(this.selectedAll) it.distinct() as ArrayList<RoutesUser>
                        else it
                    reportGeoViewModel.getRoutesFromListUsers(
                        listFiltered, reportGeoViewModel.convertDate(binding.edDateGeoLocation.text.toString())
                    )
                } else {
                    notify(requireActivity(), translateObject.findTranslate("tvNoDataGeolocation") ?: "Tiene que seleccionar una fecha y al menos un usuario")
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

        //Llamadas a los servicios
        reportGeoViewModel.getListUser()
        binding.iHeader.ivHeader.setOnClickListener {
            activity?.onBackPressed()
        }
        changeViewsFragment()
        return binding.root
    }

    override fun changeViewsFragment() {
        translateObject.apply {
            if(getInstance().isNotEmpty()) {
                binding.btnSearchGeoLocation.text = findTranslate("btnSearchGeoLocation")
                //binding.edDateGeoLocation.hint = findTranslate("edDateGeoLocation")
                binding.edUserGeoLocation.hint = findTranslate("edUserGeoLocation")
                binding.tvDateGeoLocation.text = findTranslate("tvDateGeoLocation")
                binding.tvUserGeoLocation.text = findTranslate("tvUserGeoLocation")
                Log.d(
                    "findTranslate",
                    findTranslate("tvReportGeolocation").toString() + findTranslate("tvInformCheckListFragment").toString()
                )
                binding.iHeader.title = findTranslate("tvTitleGeolocation")
                binding.iHeader.sub = findTranslate("tvSubTitleGeolocation")
            }else authViewModel.getTranslate()
        }
    }

    private fun showUser(list:List<RoutesUser>){
        listRecycleView = ArrayList<RoutesUser>()
        listRecycleView?.let { it->
            for((count, user) in list.withIndex()){
                if(count < 1 ){
                    val route = RoutesUser("1",translateObject.findTranslate("tvSelectAllAdapter") ?: "Seleccionar a todos")
                    it.add(route)
                    it.add(user)
                    backupRecycleView?.add(user)
                }
                else{
                    it.add(user)
                    backupRecycleView?.add(user)
                }
            }
            Log.d("listRecycleView",listRecycleView.toString())
        } ?: emptyList<RoutesUser>()
        recyclerAdapter = RecyclerUser(
            translateObject.findTranslate("tvSelectAllAdapter") ?: "Seleccionar a todos",
            listRecycleView!!,
            requireContext(),
            this
        )
        binding.rvUsersReportLocation.layoutManager = LinearLayoutManager(requireContext())
        binding.rvUsersReportLocation.adapter = recyclerAdapter
    }

    private fun openDatePicker(et:EditText){
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
        val dialog = InformRoutesMapDialogFragment.newInstance(this, listRoutes)
        dialog.show(childFragmentManager,"")
    }

    override fun onClick(selectedAll: Boolean) {
        this.selectedAll = selectedAll
        var namesConcant = ""
        var count = 0
        for (userChecked in recyclerAdapter?.listChecked!!) {
            if(this.selectedAll){
                namesConcant = translateObject.findTranslate("tvEveryoneSelectedAdapter") ?: "Todos seleccionados"
            }else{
                if(count == 0){
                    namesConcant = "${userChecked.name}"
                    count++
                }else namesConcant = "$namesConcant, ${userChecked.name}"
            }
        }
        binding.edUserGeoLocation.setText(namesConcant)
    }
}