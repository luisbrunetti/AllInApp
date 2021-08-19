package com.tawa.allinapp.core.platform

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.*
import com.google.android.material.snackbar.Snackbar
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.tawa.allinapp.AndroidApplication
import com.tawa.allinapp.R
import com.tawa.allinapp.core.di.ApplicationComponent
import com.tawa.allinapp.core.dialog.MessageDialogFragment
import com.tawa.allinapp.core.dialog.ProgressDialogFragment
import com.tawa.allinapp.core.extensions.appContext
import com.tawa.allinapp.core.extensions.viewContainer
import com.tawa.allinapp.core.functional.Failure
import com.tawa.allinapp.features.HomeActivity
import com.tawa.allinapp.features.auth.ui.LoginActivity
import kotlinx.android.synthetic.main.toolbar.*
import java.util.*
import javax.inject.Inject

abstract class BaseFragment : Fragment() {

    private var errorDialog: MessageDialogFragment? = null
    private var progressDialog: ProgressDialogFragment? = null

    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    var latitude :String= ""
    var longitude :String= ""

    val appComponent: ApplicationComponent by lazy(mode = LazyThreadSafetyMode.NONE) {
        (activity?.application as AndroidApplication).appComponent
    }

    val current: Date = Calendar.getInstance().time
    var year:String = ""

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

    open fun onBackPressed() {}

    internal fun firstTimeCreated(savedInstanceState: Bundle?) = savedInstanceState == null

    internal fun showProgress() = progressStatus(View.VISIBLE)

    internal fun hideProgress() = progressStatus(View.GONE)

    private fun progressStatus(viewStatus: Int) = with(activity) {
        if (this is BaseActivity) this.progress.visibility = viewStatus
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
    }

    internal fun getLastLocation(){
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

    private fun requestPermission(){
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION,android.Manifest.permission.ACCESS_FINE_LOCATION), 1010
        )
    }

    override fun onResume() {
        super.onResume()
        if (activity is HomeActivity) (activity as HomeActivity).showNavBar()
    }

    internal fun showProgressDialog() {
        progressDialog?.let {
            progressDialog?.show(childFragmentManager, "loading")
        } ?: run {
            progressDialog = ProgressDialogFragment()
            progressDialog?.show(childFragmentManager, "loading")
        }
    }

    internal fun hideProgressDialog() {
        progressDialog?.dismiss()
    }

    internal  fun getDayWeek()= when(current.day){
        0 ->  "Domingo"
        1 -> "Lunes"
        2 -> "Martes"
        3 -> "Miercoles"
        4 ->  "Jueves"
        5 ->  "Viernes"
        6 -> "Sabado"
        else -> ""
    }

    internal  fun getDayMonth() = current.date.toString()

    internal  fun getYear() = "${current.year + 1900}"

    internal  fun getMonth() = when (current.month) {
        0 -> "Enero"
        1 -> "Febrero"
        2 -> "Marzo"
        3 -> "Abril"
        4 -> "Mayo"
        5 -> "Junio"
        6 ->  "Julio"
        7 ->  "Agosto"
        8 ->  "Septiembre"
        9 ->  "Octubre"
        10 -> "Noviembre"
        11 ->  "Diciembre"
        else -> ""
    }

    open fun showMessage(message:String?){
        val dialog = MessageDialogFragment.newInstance(message ?:"")
        dialog.show(childFragmentManager, "dialog")
    }

    fun checkPermissions(){
        Dexter.withActivity(activity)
            .withPermissions(Manifest.permission.CAMERA,Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .withListener(object: MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {  }
                override fun onPermissionRationaleShouldBeShown(permissions: MutableList<PermissionRequest>?, token: PermissionToken?) {
                    token?.continuePermissionRequest()
                }
            }).check()
    }

    open fun handleFailure(failure: Failure?) {
        hideProgressDialog()
        errorDialog = null
        failure?.let {
            errorDialog = when (it) {
                is Failure.DefaultError -> {
                    MessageDialogFragment.newInstance(it.message ?: getString(R.string.error_unknown))
                }
                is Failure.NetworkConnection -> {
                    MessageDialogFragment.newInstance(getString(R.string.error_network))
                }
                else -> {
                    MessageDialogFragment.newInstance(getString(R.string.error_unknown))
                }
            }
            errorDialog?.listener = object : MessageDialogFragment.Callback {
                override fun onAccept() {}
            }
            errorDialog?.show(childFragmentManager, "dialog")
        }
    }

    fun showHome(context: Context?,selector:Boolean) = context?.let {
        val home = Intent(it, HomeActivity::class.java)
        home.putExtra("selector",selector)
        it.startActivity(home)
    }

    fun showLogin(context: Context?) = context?.let { it.startActivity(Intent(it, LoginActivity::class.java)) }

    internal fun notify(activity: FragmentActivity?,@StringRes message: Int) =
        activity?.let { Snackbar.make(it.findViewById(R.id.container), message, Snackbar.LENGTH_SHORT).show() }

    internal fun notifyWithAction(activity: FragmentActivity?, @StringRes message: Int, @StringRes actionText: Int, action: () -> Any) {
        activity?.let {
            val snackBar = Snackbar.make(it.findViewById(R.id.container), message, Snackbar.LENGTH_INDEFINITE)
            snackBar.setAction(actionText) { _ -> action.invoke() }
            snackBar.setActionTextColor(ContextCompat.getColor(appContext, R.color.colorTextPrimary))
            snackBar.show()
        }
    }

}