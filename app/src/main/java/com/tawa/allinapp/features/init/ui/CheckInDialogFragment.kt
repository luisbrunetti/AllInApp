package com.tawa.allinapp.features.init.ui

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Location
import android.location.LocationManager
import android.os.BatteryManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.fragment.app.DialogFragment
import com.google.android.gms.location.*
import com.tawa.allinapp.core.extensions.failure
import com.tawa.allinapp.core.extensions.observe
import com.tawa.allinapp.core.extensions.viewModel
import com.tawa.allinapp.core.platform.BaseFragment
import com.tawa.allinapp.databinding.DialogCheckinBinding
import com.tawa.allinapp.features.init.InitViewModel
import com.tawa.allinapp.models.Schedule
import java.sql.Timestamp
import java.text.SimpleDateFormat
import javax.inject.Inject
import kotlin.collections.ArrayList


class CheckInDialogFragment
@Inject constructor(
    private val baseFragment: BaseFragment
): DialogFragment(){

    private lateinit var binding: DialogCheckinBinding
    private  lateinit var initViewModel: InitViewModel
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    var latitude :String= ""
    var longitude :String= ""
    var idUsers = ""
    var checkState =false
    var list: List<Schedule>? = null
    private var _pv: String = ""
    private var _pvId: String = ""
    private var _description: String = ""
    var listener: Callback? = null
    private var _positionPv = -1
    var stateUser  = ArrayList<String>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DialogCheckinBinding.inflate(inflater)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        isCancelable = false
        val arrayListPv:ArrayList<String> = ArrayList()
        val aaPv = ArrayAdapter(requireActivity(), android.R.layout.simple_spinner_dropdown_item, arrayListPv)

        initViewModel = viewModel(baseFragment.viewModelFactory){
            observe(schedule, {
                it?.let {
                    list= it
                    arrayListPv.addAll(toArrayPv(it))
                    binding.pdvSpinner.adapter = aaPv
            } })
            /*observe(successCheckIn, { it?.let {
                listener?.onAccept("",_pvId,_pv, latitude,longitude,_description)
            } })*/
            observe(successGetCompanyId, { it?.let {
                initViewModel.getPv(it)
            } })
            observe(idUser, { it?.let {
                idUsers=it
            } })
            observe(stateCheck, { it?.let {
                checkState = it
            } })
            failure(failure,{
                Log.d("failure",it.toString())
            })
        }
        initViewModel.getIdCompany()
        initViewModel.getIdUser()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpBinding()
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
        newLocationData()
        requestPermission()
        getLastLocation()

        binding.pdvSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {  }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                list?.get(position)?.let { initViewModel.getStateCheck(it.pv) }
            }
        }

        binding.btnDoCheckin.setOnClickListener {
            newLocationData()
            getLastLocation()
             _positionPv  = binding.pdvSpinner.selectedItemPosition
            //Toast.makeText(context,getBatteryPercentage(requireContext()).toString()+" - "+getHour() + " - "+latitude+","+longitude,Toast.LENGTH_LONG).show()
            if(checkState)
            {
               // if(getDistance("${list[_positionPv].lat}","${list[_positionPv].long}",latitude,longitude)<=250)
              //   {
                   list?.let { list->
                       _pv = list[_positionPv].nameCorp
                       _pvId = list[_positionPv].id
                       _description = list[_positionPv].nameCorp
                       initViewModel.setPv(list[_positionPv].id,list[_positionPv].pv,list[_positionPv].nameCorp)
                       // initViewModel.setCheckIn(idUsers,list[_positionPv].pv,latitude,longitude)
                       //initViewModel.updateStatus(latitude,longitude,getBatteryPercentage(requireContext()).toString())
                       //initViewModel.sendCheck(latitude,longitude,0)
                       listener?.onAccept(idUsers,_pvId,_pv, latitude,longitude,_description,getBatteryPercentage(requireContext()).toString())
                       dismiss()
                   }
                //}
              //  else
                //   showErrorSelector()
            }
            else{
                listener?.onSnack(true)
                dismiss()
            }
        }
        binding.closeCheckInModal.setOnClickListener{
            listener?.onClose()
            dismiss()
        }

    }

    private fun getBatteryPercentage(context: Context): Int {
            val bm = context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
            return bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
    }

    private fun getHour():String{
        val formatter = SimpleDateFormat("HH:mm:ss")
        val timestamp = Timestamp(System.currentTimeMillis())
        return formatter.format(timestamp)
    }


    fun getDistance(latitudeA:String,longitudeA:String,latitudeB:String,longitudeB: String):Float{

        val latA = latitudeA.toDouble()
        val lonA = longitudeA.toDouble()
        val latB = latitudeB.toDouble()
        val lonB= longitudeB.toDouble()
        val locationA = Location("pointA")
        locationA.latitude = latA
        locationA.longitude= lonA
        val locationB = Location("pointA")
        locationB.latitude= latB
        locationB.longitude =lonB
        return   locationB.distanceTo(locationA)

    }

    private fun showErrorSelector(){
        val dialog = ErrorLocationDialogFragment()
        dialog.listener = object : ErrorLocationDialogFragment.Callback{
            override fun onAccept() {
                dismiss()
            }
        }
        dialog.show(childFragmentManager, "dialog")
    }

    private fun getLastLocation(){
        if(checkPermission()){
            if(isLocationEnabled()){
                if (ActivityCompat.checkSelfPermission(
                        requireContext(),
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        requireContext(),
                        android.Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) return
                fusedLocationProviderClient.lastLocation.addOnCompleteListener { task->
                    val location:Location? = task.result
                    if(location == null){
                        newLocationData()
                    }else{
                        Log.d("Debug:" ,"Your Location:"+ location.longitude)
                        longitude = location.longitude.toString()
                        latitude = location.latitude.toString()
                    }
                }
            }else
                Toast.makeText(context,"Please Turn on Your device Location",Toast.LENGTH_SHORT).show()
        }else
            requestPermission()
    }

    private fun checkPermission():Boolean{
        if(context?.let { ActivityCompat.checkSelfPermission(it,android.Manifest.permission.ACCESS_COARSE_LOCATION) } == PackageManager.PERMISSION_GRANTED ||
            context?.let { ActivityCompat.checkSelfPermission(it,android.Manifest.permission.ACCESS_FINE_LOCATION) } == PackageManager.PERMISSION_GRANTED)
            return true
        return false
    }

    private fun requestPermission(){
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION,android.Manifest.permission.ACCESS_FINE_LOCATION), 1010
        )
    }

    private fun isLocationEnabled():Boolean{
        val locationManager = context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private fun newLocationData(){
        val locationRequest =  LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 0
        locationRequest.fastestInterval = 0
        locationRequest.numUpdates = 1
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) return
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,locationCallback, Looper.myLooper()
        )
    }

    private val locationCallback = object : LocationCallback(){
        override fun onLocationResult(locationResult: LocationResult) {
            val lastLocation: Location = locationResult.lastLocation
            Log.d("Debug:","your last last location: "+ lastLocation.longitude.toString())
            longitude = lastLocation.longitude.toString()
            latitude = lastLocation.latitude.toString()
        }
    }

    private fun toArrayPv(list : List<Schedule>):ArrayList<String>{
        val arrayList = ArrayList<String>()
        for(element in list) {
            arrayList.add(element.nameCorp)
        }
        return  arrayList
    }

    override fun onResume() {
        super.onResume()
        val params = dialog!!.window!!.attributes
        params.width = ConstraintLayout.LayoutParams.MATCH_PARENT
        dialog!!.window!!.attributes = params as WindowManager.LayoutParams
    }

    private fun setUpBinding() {
        binding.viewModel = initViewModel
        binding.lifecycleOwner = this
        binding.executePendingBindings()
    }

    interface Callback {
        fun onAccept(idUsers:String,pvId:String, pv:String, lat:String, long:String,description:String,battery:String)
        fun onSnack(snack:Boolean)
        fun onClose()
    }
}