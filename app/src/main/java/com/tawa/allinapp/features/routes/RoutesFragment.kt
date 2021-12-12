package com.tawa.allinapp.features.routes

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.compose.ui.text.toUpperCase
import androidx.core.view.isVisible
import com.tawa.allinapp.R
import com.tawa.allinapp.core.extensions.invisible
import com.tawa.allinapp.core.extensions.observe
import com.tawa.allinapp.core.extensions.viewModel
import com.tawa.allinapp.core.platform.BaseFragment
import com.tawa.allinapp.databinding.FragmentRoutesBinding
import com.tawa.allinapp.features.init.InitViewModel
import com.tawa.allinapp.models.Routes
import com.tawa.allinapp.models.RoutesUser
import com.tawa.allinapp.models.Tracking
import kotlinx.android.synthetic.main.fragment_routes.*
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class RoutesFragment : BaseFragment() {

    private lateinit var routesViewModel: RoutesViewModel
    private lateinit var initViewModel : InitViewModel
    private lateinit var binding: FragmentRoutesBinding
    private  val formatter: SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
    private  val formatter1: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
    var timestamp: Timestamp = Timestamp(System.currentTimeMillis())
    var listUserData  = listOf<RoutesUser>()
    private var listRoutesUser  = listOf<Routes>()
    var listTrackingUser  = listOf<Tracking>()
    var dateFormat = ""
    private var _role = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentRoutesBinding.inflate(inflater)
        showProgressDialog()
        routesViewModel = viewModel(viewModelFactory) {
            observe(successGetRole,{it?.let {
                if(it.isNotEmpty()){
                    _role = it
                    routesViewModel.getListUser()
                    if(_role.toUpperCase()!="SUPERVISOR"){
                        binding.tvLabelUserRoute.invisible()
                        binding.edUserRoutes.invisible()
                        binding.edUserRoutes.isEnabled = false
                    }
                }
                hideProgressDialog()
            }})
            observe(listUser, {
                it?.let {
                    showUser(it)
                    listUserData = it
                }
            })
            observe(successGetRoutes,{it?.let {
                if(it.isNotEmpty()){
                    listRoutesUser = it
                    binding.contRoutes.isVisible = true
                    //binding.tvTypeRoute.text= "Rutas"
                    binding.tvTypeRoute.text = translateObject.findTranslate("tvTypeRoute") ?: "Rutas"
                    //binding.tvTypeRoute.text = getTypeOfLanguageById("tvTypeRoute")
                    createListRoutes(binding.listRoutes,it)
                }
                else {
                    notify(requireActivity(),"No se encontraron datos")
                }
            } })
            observe(successGetTracking,{it?.let {
                if(it.isNotEmpty()){
                    listTrackingUser = it
                    binding.contRoutes.isVisible = true
                    //binding.tvTypeRoute.text = getTypeOfLanguageById("tvTypeTracking")
                    //binding.tvTypeRoute.text= "Seguimiento"
                    binding.tvTypeRoute.text = translateObject.findTranslate("tvTypeTracking") ?: "Seguimiento"
                    createListTracking(binding.listRoutes,it)
                }
                else {
                    notify(requireActivity(),"No se encontraron datos")
                    //oast.makeText(requireContext(), "No se encontraron datos", Toast.LENGTH_SHORT).show()
                }
            } })
        }


        binding.btnBackRoutes.setOnClickListener {
            activity?.onBackPressed()
        }

        binding.edDateRoute.setOnClickListener{
            getDay(binding.edDateRoute)
        }

        binding.btnShowMapRoutes.setOnClickListener {
            if(binding.tvTypeRoute.text=="Rutas"||binding.tvTypeRoute.text=="Routes")
                showMapRoutesDialog(listRoutesUser)
            else
                showMapTrackingDialog(listTrackingUser)
        }
        routesViewModel.getRoleUser()
        binding.btnRoutes.setOnClickListener {
            if(_role.toUpperCase()=="SUPERVISOR"){
                binding.listRoutes.adapter = null
                binding.contRoutes.isVisible = false
                val user = binding.edUserRoutes.text.toString()
                if(user.isEmpty()){
                    Toast.makeText(context,"Seleccione un usuario",Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                if(dateFormat.isEmpty()){
                    Toast.makeText(context,"Seleccione una fecha valida",Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                val newList = listUserData.filter { it.name == user }
                if (newList.isNullOrEmpty()) Toast.makeText(context,"No se encontró el usuario",Toast.LENGTH_SHORT).show()
                else routesViewModel.getRoutes(newList[0].id,dateFormat,1)
            }
            else{
                binding.listRoutes.adapter= null
                binding.contRoutes.isVisible = false
                if(dateFormat.isEmpty()){
                    Toast.makeText(context,"Seleccione una fecha valida",Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                routesViewModel.getRoutes("",dateFormat,2)
            }

        }
        binding.btnTracking.setOnClickListener {
            if(_role.toUpperCase()=="SUPERVISOR"){
                binding.listRoutes.adapter= null
                binding.contRoutes.isVisible = false
                val user = binding.edUserRoutes.text.toString()
                if(user.isEmpty()){
                    Toast.makeText(context,"Seleccione un usuario",Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                if(dateFormat.isEmpty()){
                    Toast.makeText(context,"Seleccione una fecha valida",Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                val newList = listUserData.filter { it.name == user }
                if (newList.isNullOrEmpty())
                    Toast.makeText(context,"No se encontró el usuario",Toast.LENGTH_SHORT).show()
                else {
                    routesViewModel.getTracking(newList[0].id, dateFormat,1)
                }
            }else{
                binding.listRoutes.adapter= null
                binding.contRoutes.isVisible = false
                if(dateFormat.isEmpty()){
                    Toast.makeText(context,"Seleccione una fecha valida",Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                routesViewModel.getTracking("", dateFormat,2)
            }
        }
        binding.edDateRoute.setText(formatter.format(timestamp))
        dateFormat = formatter1.format(timestamp)
        changeViewsFragment()
        return binding.root
    }

    override fun changeViewsFragment() {
        translateObject.apply{
            if(getInstance().isNotEmpty()) {
                binding.textView31.text = findTranslate("tvTypeRoute")

                binding.tvLabelUserRoute.text = findTranslate("tvLabelUserRoute")

                binding.edUserRoutes.hint = findTranslate("edUserRoutes")

                binding.tvLabelSelectDateRoute.text = findTranslate("tvLabelSelectDateRoute")

                binding.edDateRoute.hint = findTranslate("edDateRoute")

                binding.btnRoutes.text = findTranslate("btnRoutes")

                binding.btnTracking.text = findTranslate("btnTracking")

                binding.btnShowMapRoutes.text = findTranslate("btnShowMapRoutes")

                binding.tvPdvRoute.text = findTranslate("tvPdvRoute")

            }else authViewModel.getTranslate()
        }
    }
    private fun showUser(list:List<RoutesUser>){
        val listUser  = ArrayList<String>()
        for(user in list)
        {
            listUser.add(user.name)
        }
        val userAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1,listUser)
        binding.edUserRoutes.setAdapter(userAdapter)
    }

    private fun showMapRoutesDialog(listRoutes:List<Routes>){
        val dialog = RoutesMapDialogFragment.newInstance(listRoutes)
        dialog.show(childFragmentManager,"")
    }

    private fun showMapTrackingDialog(listTracking:List<Tracking>){
        val dialog = TrackingMapDialogFragment.newInstance(listTracking)
        dialog.show(childFragmentManager,"")
    }

    private fun createListRoutes(listView: ListView,list:List<Routes>){
        val listRoutes  = ArrayList<String>()
        for(route in list)
            listRoutes.add(route.nameCorpPv?:"")
        val aa = ArrayAdapter(requireContext(), R.layout.item_routes,listRoutes)
        listView.adapter = aa
    }
    private fun createListTracking(listView: ListView,list:List<Tracking>){
        val listTracking = ArrayList<String>()
        for(track in list)
            listTracking.add( track.nameCorpPv)
        val aa = ArrayAdapter(requireContext(), R.layout.item_routes,listTracking)
        listView.adapter = aa
    }

    private fun getDay(et: EditText){
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val dpd = DatePickerDialog(requireContext(), { _, _, monthOfYear, dayOfMonth ->

            et.setText("" + dayOfMonth + "/" + getMonth(monthOfYear) + "/" + year)
            dateFormat = ""+year+"-"+getMonth(monthOfYear)+"-"+dayOfMonth
        }, year, month, day)
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