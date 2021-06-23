package com.tawa.allinapp.features.init.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.tawa.allinapp.R
import com.tawa.allinapp.core.extensions.failure
import com.tawa.allinapp.core.extensions.observe
import com.tawa.allinapp.core.extensions.viewModel
import com.tawa.allinapp.core.platform.BaseFragment
import com.tawa.allinapp.databinding.FragmentInitBinding
import com.tawa.allinapp.features.init.InitViewModel


class InitFragment : BaseFragment() {

    private lateinit var initViewModel: InitViewModel
    private lateinit var binding: FragmentInitBinding
    private lateinit var locationManager:LocationManager

    private var checkOutDialog: CheckOutDialogFragment? = null
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var checkIn:Boolean = true
    var _user = ""
    private lateinit var _pvId: String
    private lateinit var _pv: String
    private lateinit var _lat: String
    private lateinit var _long: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentInitBinding.inflate(inflater)
        showSelector()
        initViewModel = viewModel(viewModelFactory) {
            observe(dayState, { it?.let { if(it) {
                val currentDay = getString(R.string.current_day, getDayWeek(),getDayMonth(),getMonth(),getYear())
                binding.currentDay.text  = currentDay
            } }})
            observe(checkInMode, { it?.let {
                checkIn = it
                hideProgressDialog()
            }})
            observe(idUser, { it?.let {
                _user = it
            }})
            observe(pvDesc, { it?.let {
                _pv = it; if(::_pvId.isInitialized) showCheckOut()
            }})
            observe(pvId, { it?.let {
                _pvId = it; if(::_pv.isInitialized) showCheckOut()
            }})
            observe(successCheckOut, { it?.let {
                initViewModel.getCheckMode()
            } })
            failure(failure, ::handleFailure)
        }
        initViewModel.getIdUser()
        binding.btCheckIn.setOnClickListener{
            if(checkIn) showSelectorCheckIn()
            else {
                initViewModel.getDescPV()
                initViewModel.getIdPV()
            }
        }
        return binding.root
    }

    private fun showSelector(){
        val dialog = SelectorDialogFragment(this)
        dialog.show(childFragmentManager, "dialog")
    }

    private fun showSelectorCheckIn(){
        val dialog = CheckInDialogFragment(this)
        dialog.listener = object : CheckInDialogFragment.Callback {
            override fun onAccept(pvId:String, pv:String,lat:String, long:String) {
                _pv = pv; _lat = lat; _long = long; _pvId = pvId
                initViewModel.getCheckMode()
            }
        }
        dialog.show(childFragmentManager, "dialog")
    }

    private fun showCheckOut(){
        getActualLocation()
        checkOutDialog = CheckOutDialogFragment.newInstance(_pv, _user)
        checkOutDialog?.listener = object : CheckOutDialogFragment.Callback {
            override fun onAccept() {
                initViewModel.setCheckOut(_user,_pvId,_lat,_long)
            }
        }
        checkOutDialog?.show(childFragmentManager, "checkOutDialog")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpBinding()
    }

    private fun getActualLocation() {
        locationManager = context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) return
        val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        _lat = location?.latitude.toString()
        _long = location?.longitude.toString()
    }

    private fun setUpBinding() {
        binding.viewModel = initViewModel
        binding.lifecycleOwner = this
        binding.executePendingBindings()
    }
}



