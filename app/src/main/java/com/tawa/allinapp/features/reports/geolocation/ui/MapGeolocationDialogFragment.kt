package com.tawa.allinapp.features.reports.geolocation.ui

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.gson.Gson
import com.tawa.allinapp.R
import com.tawa.allinapp.databinding.FragmentInformRoutesMapDialogBinding
import com.tawa.allinapp.models.RoutesInform
import com.tawa.allinapp.models.TrackingInform
import java.lang.Exception
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil
import com.tawa.allinapp.core.dialog.MessageDialogFragment
import com.tawa.allinapp.core.platform.BaseFragment
import com.tawa.allinapp.features.reports.geolocation.Constants
import com.tawa.allinapp.models.Tracking
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList


class InformRoutesMapDialogFragment
@Inject constructor(val baseFragment: BaseFragment): DialogFragment(), GoogleMap.OnInfoWindowClickListener {

    private lateinit var binding: FragmentInformRoutesMapDialogBinding
    private var listRoutes: List<TrackingInform>? = null
    private var listMarkers : ArrayList<Marker?>? = null
    private var hashMapMarkerRoute : MutableMap<String, InfoGeolocation>? = null
    private var hmPvsInfoWindow: MutableMap<String, InfoWindowPv> = mutableMapOf()
    private var arrayMarkerInfoWindow: ArrayList<InfoWindowChecks> = ArrayList()
    private var lastLatLng: LatLng? = null
    companion object {
        const val LIST_ROUTES_INFORM = "list_routes_inform"
        fun newInstance(baseFragment: BaseFragment, listRoutes : List<TrackingInform>): InformRoutesMapDialogFragment {
            val frag = InformRoutesMapDialogFragment(baseFragment)
            val bundle = Bundle()
            val jsonParsed = Gson().toJson(listRoutes)
            bundle.putString(LIST_ROUTES_INFORM,jsonParsed)
            frag.arguments = bundle
            return frag
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private val mapCallBack = OnMapReadyCallback { googleMap ->
        googleMap.apply {
            setOnInfoWindowClickListener(this@InformRoutesMapDialogFragment)
            mapType = GoogleMap.MAP_TYPE_NORMAL
            uiSettings.isZoomControlsEnabled = true
            setInfoWindowAdapter(object : GoogleMap.InfoWindowAdapter {
                @SuppressLint("SetTextI18n")
                override fun getInfoWindow(p0: Marker): View? {
                    var v: View? = null

                    hmPvsInfoWindow[p0.id]?.let {
                        v = layoutInflater.inflate(R.layout.info_adapter_pv_geolocation, null)
                        val tvSalesPoint = v?.findViewById<TextView>(R.id.tvSalesPointInfoAdapterPV)
                        val tvDirPv = v?.findViewById<TextView>(R.id.tvDirInfoAdapterPv)
                        val checkToDo = v?.findViewById<TextView>(R.id.tvCheckInToDoAdapterPv)
                        val checkFinished =
                            v?.findViewById<TextView>(R.id.tvCheckInFinshiedAdapterPV)
                        val taskToDo = v?.findViewById<TextView>(R.id.tvTasksTodoAdapterPv)
                        val taskFinished = v?.findViewById<TextView>(R.id.tvTasksFinishedAdapterPv)
                        tvSalesPoint?.text = "${it.pv} - ${it.codPv}"
                        tvDirPv?.text = it.dirPv
                        val checkInToDo = it.checkInToDo
                        val checkInFinished = it.checkInFinished
                        val checkOutToDo = it.checkOutToDo
                        val checkOutFinished = it.checkOutFinished
                        checkToDo?.text = "Check in pendientes/realizados: $checkInToDo/$checkInFinished"
                        checkFinished?.text = "Check out pendientes/realizados: $checkOutToDo/$checkOutFinished"
                        taskToDo?.text = "Tareas pendientes : ${it.tasksTodo}"
                        taskFinished?.text = "Tareas realizadas : ${it.taskFinished}"

                    }
                    for (infoWindow in arrayMarkerInfoWindow) {
                        if (infoWindow.markerId == p0.id) {
                            v = layoutInflater.inflate(R.layout.info_adapter_geolocation, null)
                            val tvLatLng = v?.findViewById<TextView>(R.id.tvDescInfoAdapter)
                            val tvSalesPoint =
                                v?.findViewById<TextView>(R.id.tvSalesPointInfoAdapter)
                            val tvType = v?.findViewById<TextView>(R.id.tvTypeInfoAdapter)
                            tvType?.text = infoWindow.typePoint
                            tvSalesPoint?.text = "${infoWindow.pv} - ${infoWindow.codPv}"
                            tvLatLng?.text = "(" + p0.position.latitude.toString()
                                .substring(0, 9) + "," + p0.position.longitude.toString()
                                .substring(0, 9) + ")"
                        }
                    }
                    return v
                }

                @SuppressLint("SetTextI18n")
                override fun getInfoContents(p0: Marker): View? {
                    return null
                }

            })
        }

        hashMapMarkerRoute = mutableMapOf()
        listMarkers = ArrayList()
        var firstTime = true
        var lat : Double? = null
        var long: Double? = null
        val pvs = ArrayList<String>()
        try{

        listRoutes?.let { listInformTracking ->
            for (userTracking in listInformTracking) {
                Log.d("UserTracking", "UserTracking -> ${userTracking.nameUser} List -> ${userTracking.listTracking}")
                for (tracking in userTracking.listTracking) {
                    for (visit in tracking.visits) {
                        //for(visit in visits?.visit!!){
                        var markerId: String = ""
                        var markerType: String = ""
                        when (visit?.comment?.uppercase()) {
                            Constants.CHECK_IN -> {
                                val userPosition =
                                    LatLng(visit.latitude.toDouble(), visit.longitude.toDouble())
                                val iconD = resources.getDrawable(R.drawable.ic_marker_checkin)
                                val marker = googleMap.addMarker(
                                    MarkerOptions()
                                        .position(userPosition)
                                        .infoWindowAnchor(0f, -0.1f)
                                        .icon(getMarkerIconFromDrawable(iconD))
                                )
                                markerId = marker.id
                                markerType = Constants.CHECK_IN_VIEW
                                hashMapMarkerRoute!![marker.id] = InfoGeolocation(
                                    Constants.CHECK_IN,
                                    visit.creation,
                                    userTracking.nameUser,
                                    tracking.codPvCop,
                                    tracking.dirCorpPv,
                                    tracking.Pv
                                )
                            }
                            Constants.CHECK_OUT -> {
                                val userPosition =
                                    LatLng(visit.latitude.toDouble(), visit.longitude.toDouble())
                                Log.d("pos", userPosition.toString())
                                val iconD = resources.getDrawable(R.drawable.ic_marker_checkout)
                                val marker = googleMap.addMarker(
                                    MarkerOptions()
                                        .position(userPosition)
                                        .icon(getMarkerIconFromDrawable(iconD))
                                )
                                markerId = marker.id
                                markerType = Constants.CHECK_OUT_VIEW
                                hashMapMarkerRoute!![marker.id] = InfoGeolocation(
                                    Constants.CHECK_OUT,
                                    visit.creation,
                                    userTracking.nameUser,
                                    tracking.codPvCop,
                                    tracking.dirCorpPv,
                                    tracking.Pv
                                )
                            }
                            Constants.NONE_REPORT -> {
                                val userPosition =
                                    LatLng(visit.latitude.toDouble(), visit.longitude.toDouble())
                                Log.d("pos", userPosition.toString())
                                val iconD = resources.getDrawable(R.drawable.ic_marker_routes)
                                val marker = googleMap.addMarker(
                                    MarkerOptions()
                                        .position(userPosition)
                                        .icon(getMarkerIconFromDrawable(iconD))
                                )
                                markerType = Constants.POINT_SALE
                                hashMapMarkerRoute!![marker.id] = InfoGeolocation(
                                    Constants.NONE_REPORT,
                                    visit.creation,
                                    userTracking.nameUser,
                                    tracking.codPvCop,
                                    tracking.dirCorpPv,
                                    tracking.Pv
                                )
                            }
                        }
                        arrayMarkerInfoWindow.add(InfoWindowChecks(markerId, tracking.Pv, tracking.codPvCop, tracking.dirCorpPv, markerType))
                    }
                    for (task in tracking.tasks){
                        when(task?.reportState?.uppercase()){
                            Constants.REPORT_COMPLETED -> {
                                val userPosition = LatLng(task.latitude.toDouble(), task.longitude.toDouble())
                                val iconD = resources.getDrawable(R.drawable.ic_marker_reports)
                                val marker = googleMap.addMarker(MarkerOptions()
                                    .position(userPosition)
                                    .icon(getMarkerIconFromDrawable(iconD))
                                )
                                arrayMarkerInfoWindow.add(InfoWindowChecks(marker.id, tracking.Pv, tracking.codPvCop, tracking.dirCorpPv, Constants.REPORTED_VIEW))
                                hashMapMarkerRoute!![marker.id] = InfoGeolocation(Constants.REPORT_COMPLETED,task.creation, userTracking.nameUser,tracking.codPvCop,tracking.dirCorpPv,tracking.Pv)
                            }
                        }
                    }
                    if (firstTime) {
                        lat = tracking.latitude
                        long = tracking.longitude
                        firstTime = false
                    }

                    if(!pvs.contains(tracking.Pv)){
                        createMarkerPv(googleMap,tracking)
                        pvs.add(tracking.Pv)
                    }
                    else sumValues(tracking)
                }
            }
            checkPdvFinished()
            if (lat != null && long != null) {
                //Log.d("camara","entre")
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(lat!!, long!!)))
                googleMap.setMinZoomPreference(10f)
            }}}
        catch (e : Exception){
            MessageDialogFragment.newInstance(baseFragment,baseFragment.translateObject.findTranslate("tvErrorMessageFrag") ?: "Hubo un error")
        }
    }
    @SuppressLint("UseCompatLoadingForDrawables")
    private fun checkPdvFinished(){
        for(key in hmPvsInfoWindow.keys){
            val item = hmPvsInfoWindow[key]
            if( (item?.checkInToDo == 0) &&
                (item.checkOutToDo == 0) &&
                (item.tasksTodo == 0)){
                item.marker.setIcon(getMarkerIconFromDrawable(resources.getDrawable(R.drawable.ic_marker_routes_finished)))
            }
        }
    }

    private fun createMarkerPv(
        googleMap: GoogleMap,
        tracking: Tracking,
    ) {
        tracking.let { track ->
            LatLng(track.latitude!!,track.longitude!!).let {
                val pvPosition = it
                val iconD = resources.getDrawable(R.drawable.ic_marker_routes)
                lastLatLng = convertLatLngInMeter(pvPosition)
                val marker = googleMap.addMarker(MarkerOptions()
                    .position(lastLatLng)
                    .infoWindowAnchor(0f,-0.1f)
                    .icon(getMarkerIconFromDrawable(iconD))
                )
                hmPvsInfoWindow[marker.id] = InfoWindowPv(
                    marker = marker,
                    track.Pv, track.codPvCop,
                    track.dirCorpPv, Constants.POINT_SALE,
                    1,
                    track.checks.checkIn.pendientes,
                    track.checks.checkIn.concluidas,
                    track.checks.checkOut.pendientes,
                    track.checks.checkOut.concluidas,
                    track.reports.tareasPendientes,
                    track.reports.tareasCompletadas)
            }
        }

    }

    private fun sumValues(tracking: Tracking) {
        Log.d("hmPvsInfoWindow", hmPvsInfoWindow.toString())
        for((key,value) in hmPvsInfoWindow){
            if(value.pv == tracking.Pv) {
                value.taskFinished = value.taskFinished.plus(tracking.reports.tareasCompletadas)
                value.tasksTodo = value.tasksTodo.plus(tracking.reports.tareasPendientes)
                value.checkInToDo = value.checkInToDo.plus(tracking.checks.checkIn.pendientes)
                value.checkInFinished = value.checkInFinished.plus(tracking.checks.checkIn.concluidas)
                value.checkOutToDo = value.checkOutToDo.plus(tracking.checks.checkOut.pendientes)
                value.checkOutFinished = value.checkOutFinished.plus(tracking.checks.checkOut.concluidas)
                value.total = value.total + 1
            }
        }
    }


    private fun convertLatLngInMeter(oldLatLng: LatLng) : LatLng{
        val r = Random()
        val randomHeading: Double = r.nextInt(360).toDouble()
        return SphericalUtil.computeOffset(oldLatLng, 20.0, randomHeading)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInformRoutesMapDialogBinding.inflate(inflater)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            val mapFragment = childFragmentManager.findFragmentById(R.id.map_dialog_routes_inform) as SupportMapFragment?
            mapFragment?.let { mapFragment ->
                mapFragment.getMapAsync(mapCallBack)
                arguments?.getString(LIST_ROUTES_INFORM)?.let { jsonParsed ->
                    Gson().fromJson(jsonParsed, Array<TrackingInform>::class.java).toList().let {
                        listRoutes = it
                    } ?: emptyList<RoutesInform>()
                }
            }
            binding.btnCloseMapRoutes.setOnClickListener { dismiss() }
        } catch (e: Exception) {
            Log.d("InformRoutes", "Ha ocurrido un error")
            dismiss()
        }
    }

    override fun onResume() {
        super.onResume()
        val params = dialog!!.window!!.attributes
        params.width = ConstraintLayout.LayoutParams.MATCH_PARENT
        dialog!!.window!!.attributes = params as android.view.WindowManager.LayoutParams
    }

    private fun getMarkerIconFromDrawable(drawable: Drawable): BitmapDescriptor? {
        val canvas = Canvas()
        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        canvas.setBitmap(bitmap)
        drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
        drawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }
    interface Callback{
        fun onAccept()
    }
    override fun onInfoWindowClick(p0: Marker) {
        val infoGeolocation = hashMapMarkerRoute?.get(p0.id)
        infoGeolocation?.let {
            val dialog = InfoGeolocationDialogFragment.newInstance(baseFragment, it)
            dialog.show(childFragmentManager, "dialog")
        }
    }
}

data class InfoGeolocation(
    val type:String,
    val time:String,
    val user:String,
    val pv_cod:String,
    val pv_dir:String,
    val pv_desc:String
)

data class InfoWindowPv(
    val marker: Marker,
    val pv:String,
    val codPv:String,
    val dirPv:String,
    val typePoint:String,
    var total: Int,
    var checkInToDo: Int,
    var checkInFinished:Int,
    var checkOutToDo:Int,
    var checkOutFinished:Int,
    var tasksTodo:Int,
    var taskFinished:Int
)

data class InfoWindowChecks(
    val markerId: String,
    val pv:String,
    val codPv:String,
    val dirPv:String,
    val typePoint:String
)