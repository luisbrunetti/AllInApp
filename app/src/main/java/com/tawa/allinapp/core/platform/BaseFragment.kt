package com.tawa.allinapp.core.platform

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
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
import com.tawa.allinapp.core.extensions.viewModel
import com.tawa.allinapp.core.functional.Failure
import com.tawa.allinapp.features.HomeActivity
import com.tawa.allinapp.features.auth.AuthViewModel
import com.tawa.allinapp.features.auth.ui.LoginActivity
import com.tawa.allinapp.models.Translate
import com.tawa.allinapp.models.TranslateItem
import kotlinx.android.synthetic.main.toolbar.*
import java.util.*
import javax.inject.Inject

abstract class BaseFragment : Fragment() {

    private var errorDialog: MessageDialogFragment? = null
    private var progressDialog: ProgressDialogFragment? = null

    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    var latitude :String= ""
    var longitude :String= ""


    companion object {
        const val SPANISH : String = "SPANISH"
        const val ENGLISH : String = "ENGLISH"
        var CURRENT_LANGUAGE: String = SPANISH
    }
    var languagePosition: Int = 0

    protected var mDay: Int? = null
    protected var mMonth: Int? = null
    protected var mYear: Int? = null


    val appComponent: ApplicationComponent by lazy(mode = LazyThreadSafetyMode.NONE) {
        (activity?.application as AndroidApplication).appComponent
    }

    val current: Date = Calendar.getInstance().time
    var year:String = ""

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject lateinit var translateObject: TranslateObject


    open fun onBackPressed() {

    }

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

    internal fun showNotification(message:String,channelS: String){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "ch2"
            val descriptionText = "descrip"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelS, name, importance).apply {
                description = descriptionText
            }
            val nm = requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            nm.createNotificationChannel(channel)
            nm.sendNotification(message,requireContext(),channelS)
        }
    }

    private fun NotificationManager.sendNotification(messageBody: String, applicationContext: Context,channel:String) {
        val builder = NotificationCompat.Builder(
            applicationContext,
            channel
        )
            .setLargeIcon(BitmapFactory.decodeResource(requireContext().resources, R.mipmap.ic_launcher))
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentText(messageBody)
            .setStyle(NotificationCompat.BigTextStyle().bigText(messageBody))
        notify(0, builder.build())
    }

    internal fun getLastLocation(){
        if(checkPermission()){
            if(isLocationEnabled()){
                if (ActivityCompat.checkSelfPermission(requireContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) return

                fusedLocationProviderClient.lastLocation.addOnCompleteListener { task->
                    val location:Location? = task.result
                    if(location == null){
                        newLocationData()
                    }else{
                        Log.d("Debug:" ,"Your Location:"+ location.latitude + " , "+ location.longitude)
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
        if(context?.let { ActivityCompat.checkSelfPermission(it,Manifest.permission.ACCESS_COARSE_LOCATION) } == PackageManager.PERMISSION_GRANTED ||
            context?.let { ActivityCompat.checkSelfPermission(it,Manifest.permission.ACCESS_FINE_LOCATION) } == PackageManager.PERMISSION_GRANTED)
            return true
        return false
    }

    open fun isLocationEnabled():Boolean{
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
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) return
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,locationCallback, Looper.myLooper()
        )
    }

    private val locationCallback = object : LocationCallback(){
        override fun onLocationResult(locationResult: LocationResult) {
            val lastLocation: Location = locationResult.lastLocation
            //Log.d("Debug:","your last last location: "+ lastLocation.longitude.toString())
            longitude = lastLocation.longitude.toString()
            latitude = lastLocation.latitude.toString()
        }
    }

    private fun requestPermission(){
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION), 1010
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

    internal fun hideProgressDialog(){
        progressDialog?.dismiss()
    }

    internal  fun getDayWeek() : String{
        translateObject.apply{
            return when(current.day){
                0 ->  findTranslate("tvSunday") ?: "Domingo"
                1 -> findTranslate("tvMonday") ?: "Lunes"
                2 -> findTranslate("tvTuesday") ?: "Martes"
                3 -> findTranslate("tvWednesday") ?:"Miercoles"
                4 ->  findTranslate("tvThursday") ?: "Jueves"
                5 ->  findTranslate("tvFriday") ?: "Viernes"
                6 -> findTranslate("tvSaturday")?: "Sabado"
                else -> ""
            }
        }
    }

    internal  fun getDayMonth() = current.date.toString()

    internal  fun getYear() = "${current.year + 1900}"

    internal fun getMonth() : String {
        translateObject.apply {
            return when (current.month) {
                0 -> findTranslate("tvJanuary")?:"Enero"
                1 -> findTranslate("tvFebrary")?:"Febrero"
                2 -> findTranslate("tvMarch")?:"Marzo"
                3 -> findTranslate("tvApril")?:"Abril"
                4 -> findTranslate("tvMay") ?:"Mayo"
                5 -> findTranslate("tvJune")?:"Junio"
                6 -> findTranslate("tvJuly")?:"Julio"
                7 -> findTranslate("tvAugust")?:"Agosto"
                8 -> findTranslate("tvSeptember")?:"Septiembre"
                9 -> findTranslate("tvOctober")?:"Octubre"
                10 -> findTranslate("tvNovember")?:"Noviembre"
                11 -> findTranslate("tvDecember")?:"Diciembre"
                else -> ""
            }
        }
    }


    internal fun getMonthByNumber(monthYear: Int) = when(monthYear){
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

    open fun showMessage(message:String?){
        val dialog = MessageDialogFragment.newInstance(this,message ?:"")
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
                    MessageDialogFragment.newInstance(this,it.message ?: getString(R.string.error_unknown))
                }
                is Failure.NetworkConnection -> {
                    MessageDialogFragment.newInstance(this,getString(R.string.error_network))
                }
                else -> {
                    MessageDialogFragment.newInstance(this,getString(R.string.error_unknown))
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

    internal fun notify(activity: FragmentActivity?,message: String) =
        activity?.let { Snackbar.make(it.findViewById(R.id.container), message, Snackbar.LENGTH_SHORT).show() }

    internal fun notifyWithAction(activity: FragmentActivity?, @StringRes message: Int, @StringRes actionText: Int, action: () -> Any) {
        activity?.let {
            val snackBar = Snackbar.make(it.findViewById(R.id.container), message, Snackbar.LENGTH_INDEFINITE)
            snackBar.setAction(actionText) { _ -> action.invoke() }
            snackBar.setActionTextColor(ContextCompat.getColor(requireContext(), R.color.colorTextPrimary))
            snackBar.show()
        }
    }

    internal fun updateCurrentDate(){
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        this.mDay = day
        this.mMonth = month +1
        this.mYear = year
    }


    abstract fun changeViewsFragment()

}