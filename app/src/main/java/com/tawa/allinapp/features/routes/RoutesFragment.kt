package com.tawa.allinapp.features.routes

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import com.tawa.allinapp.R
import com.tawa.allinapp.core.extensions.observe
import com.tawa.allinapp.core.extensions.viewModel
import com.tawa.allinapp.core.platform.BaseFragment
import com.tawa.allinapp.databinding.FragmentRoutesBinding
import com.tawa.allinapp.models.ReportStatus
import com.tawa.allinapp.models.RoutesUser
import kotlinx.android.synthetic.main.fragment_routes.*
import java.util.*
import kotlin.collections.ArrayList


class RoutesFragment : BaseFragment() {

    private lateinit var routesViewModel: RoutesViewModel
    private lateinit var binding: FragmentRoutesBinding
    var listUserData  = listOf<RoutesUser>()
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
                    createListRoutes(binding.listRoutes,it)
                }
            })
        }
        binding.edDateUserRoutes.setOnClickListener{
            getDay(binding.edDateUserRoutes)
        }
        binding.btnShowMapRoutes.setOnClickListener {
            showMapRoutesDialog()
        }
        routesViewModel.getListUser()
        //val listRoutes = arrayListOf<String>("Ruta 1","Ruta 2","Ruta 3","Ruta 4","Ruta 5")
        //createListRoutes(binding.listRoutes,listRoutes)
        binding.btnRoutes.setOnClickListener {
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
            {
                Toast.makeText(context,"No se encontró el usuario",Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(context,newList[0].id,Toast.LENGTH_SHORT).show()

            }

        }
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

    private fun showMapRoutesDialog(){
        val dialog = RoutesMapDialogFragment.newInstance("-9.517005","-77.528855","Prueba rutas")
        dialog.show(childFragmentManager,"")
    }

    private fun createListRoutes(listView: ListView,list:List<RoutesUser>){
        val listUser  = ArrayList<String>()
        for(user in list)
        {
            listUser.add(user.name)
        }
        val aa = ArrayAdapter(requireContext(), R.layout.item_routes,listUser)
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