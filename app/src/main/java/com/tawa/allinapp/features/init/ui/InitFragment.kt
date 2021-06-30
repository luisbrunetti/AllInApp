package com.tawa.allinapp.features.init.ui


import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.skydoves.balloon.ArrowOrientation
import com.skydoves.balloon.BalloonAnimation
import com.skydoves.balloon.createBalloon
import com.skydoves.balloon.overlay.BalloonOverlayRect
import com.skydoves.balloon.showBalloon
import com.tawa.allinapp.R
import com.tawa.allinapp.core.extensions.failure
import com.tawa.allinapp.core.extensions.observe
import com.tawa.allinapp.core.extensions.viewModel
import com.tawa.allinapp.core.platform.BaseFragment
import com.tawa.allinapp.databinding.FragmentInitBinding
import com.tawa.allinapp.features.HomeActivity
import com.tawa.allinapp.features.init.InitViewModel


class InitFragment : BaseFragment() {

    private lateinit var initViewModel: InitViewModel
    private lateinit var binding: FragmentInitBinding
    private lateinit var locationManager:LocationManager

    private var checkOutDialog: CheckOutDialogFragment? = null
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var checkIn:Boolean = true
    private var _user = ""
    private lateinit var _pvId: String
    private lateinit var _pv: String
    private lateinit var _lat: String
    private lateinit var _long: String
    private var selector = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentInitBinding.inflate(inflater)
        showProgressDialog()
        selector = activity?.intent?.getBooleanExtra("selector",false)?:false
        if(selector) showSelector()

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
                _pv = it
                initViewModel.getIdPV()
            }})
            observe(pvId, { it?.let {
                _pvId = it
                showCheckOut()
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
            }
        }
        binding.btnToolTip.setOnClickListener{
            showToolTip()
        }
        binding.viewBtnRoutes.setOnClickListener {
            findNavController().navigate(InitFragmentDirections.actionNavigationInitToNavigationRoutes())
        }
        binding.viewBtnPV.setOnClickListener {
            findNavController().navigate(InitFragmentDirections.actionNavigationInitToNavigationPdv())
        }
        binding.viewBtnCalendar.setOnClickListener {
            findNavController().navigate(InitFragmentDirections.actionNavigationInitToNavigationCalendar())
        }
        binding.viewBtnReports.setOnClickListener {
            findNavController().navigate(InitFragmentDirections.actionNavigationInitToNavigationReports())
        }
        return binding.root
    }

    private fun showSelector(){
        val dialog = SelectorDialogFragment(this)
        dialog.show(childFragmentManager, "dialog")
    }

    private  fun showToolTip(){
        val balloon = createBalloon(requireContext()) {
            setLayout(R.layout.home_options)
            setArrowSize(0)
            setWidthRatio(0.92f)
            setHeight(182)
            setMarginTop(118)
            setMarginRight(36)
            setCornerRadius(0f)
            setAlpha(1f)
            setBackgroundColorResource(R.color.white)
            setBalloonAnimation(BalloonAnimation.OVERSHOOT)
            setLifecycleOwner(lifecycleOwner)
            setArrowOrientation(ArrowOrientation.RIGHT)
           // setIsVisibleOverlay(true)
            setOverlayColorResource(R.color.bg_overlay)
            setOverlayShape(BalloonOverlayRect).build()
        }
        binding.btnToolTip.showBalloon(balloon)

    }
    private fun showSelectorCheckIn(){
        val dialog = CheckInDialogFragment(this)
        dialog.listener = object : CheckInDialogFragment.Callback {
            override fun onAccept(pvId:String, pv:String,lat:String, long:String) {
                _pv = pv; _lat = lat; _long = long; _pvId = pvId
                initViewModel.getCheckMode()
            }
            override fun onSnack(snack: Boolean) {
                if (snack) notify(activity,R.string.notify_already)
            }
        }
        dialog.show(childFragmentManager, "dialog")
    }

    private fun showCheckOut(){
        checkOutDialog = CheckOutDialogFragment.newInstance(_pv, _user)
        checkOutDialog?.listener = object : CheckOutDialogFragment.Callback {
            override fun onAccept() {
                showProgressDialog()
                getActualLocation()
                initViewModel.setCheckOut(_user,_pvId,_lat,_long)
            }
        }
        checkOutDialog?.show(childFragmentManager, "checkOutDialog")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpBinding()
        hideProgressDialog()
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

    override fun onResume() {
        super.onResume()
        (activity as HomeActivity).hideNavBar()
    }

    private fun setUpBinding() {
        binding.viewModel = initViewModel
        binding.lifecycleOwner = this
        binding.executePendingBindings()
    }
}



