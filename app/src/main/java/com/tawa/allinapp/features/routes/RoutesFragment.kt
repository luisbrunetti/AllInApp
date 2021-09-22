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
import androidx.core.view.isVisible
import com.tawa.allinapp.R
import com.tawa.allinapp.core.extensions.observe
import com.tawa.allinapp.core.extensions.viewModel
import com.tawa.allinapp.core.platform.BaseFragment
import com.tawa.allinapp.databinding.FragmentRoutesBinding
import com.tawa.allinapp.models.ReportStatus
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
    private lateinit var binding: FragmentRoutesBinding
    private  val formatter: SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
    private  val formatter1: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
    var timestamp: Timestamp = Timestamp(System.currentTimeMillis())
    var listUserData  = listOf<RoutesUser>()
    private var listRoutesUser  = listOf<Routes>()
    var listTrackingUser  = listOf<Tracking>()
    var dateFormat = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentRoutesBinding.inflate(inflater)

        routesViewModel = viewModel(viewModelFactory) {
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
                    binding.tvType.text = "Rutas"
                    createListRoutes(binding.listRoutes,it)
                }
                else {
                    Toast.makeText(context, "No se encontraron datos", Toast.LENGTH_SHORT).show()
                }
            } })
            observe(successGetTracking,{it?.let {
                if(it.isNotEmpty()){
                    listTrackingUser = it
                    binding.contRoutes.isVisible = true
                    binding.tvType.text = "Seguimiento"
                    createListTracking(binding.listRoutes,it)
                }
                else {
                    Toast.makeText(context, "No se encontraron datos", Toast.LENGTH_SHORT).show()
                }
            } })

        }
        binding.btnBackRoutes.setOnClickListener {
            activity?.onBackPressed()
        }
        binding.edDateUserRoutes.setOnClickListener{
            getDay(binding.edDateUserRoutes)
        }
        binding.btnShowMapRoutes.setOnClickListener {
            if(binding.tvType.text=="Rutas")
                showMapRoutesDialog(listRoutesUser)
            else
                showMapTrackingDialog(listTrackingUser)
        }
        routesViewModel.getListUser()
        binding.btnRoutes.setOnClickListener {
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
            else
                routesViewModel.getRoutes(newList[0].id,dateFormat)
        }
        binding.btnTracking.setOnClickListener {
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
                routesViewModel.getTracking(newList[0].id, dateFormat)
            }
        }
        binding.edDateUserRoutes.setText(formatter.format(timestamp))
        dateFormat = formatter1.format(timestamp)
        return binding.root
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