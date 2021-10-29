package com.tawa.allinapp.features.reports.geolocation

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
import android.widget.ImageView
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
import com.tawa.allinapp.models.Tracking
import com.tawa.allinapp.models.TrackingInform
import java.lang.Exception
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil
import java.util.*
import kotlin.collections.ArrayList


class InformRoutesMapDialogFragment : DialogFragment(), GoogleMap.OnInfoWindowClickListener {

    private lateinit var binding: FragmentInformRoutesMapDialogBinding
    private var listRoutes: List<TrackingInform>? = null
    private var listMarkers : ArrayList<Marker?>? = null
    private var hashMapMarkerRoute : MutableMap<String, InfoGeolocation>? = null
    private var hmMarkerInfoWindow: MutableMap<String, InfoWindow> = mutableMapOf()
    private var lastLatLng: LatLng? = null
    companion object {
        const val LIST_ROUTES_INFORM = "list_routes_inform"
        const val CHECK_IN = "Check in"
        const val CHECK_OUT = "Check out"
        const val REPORTED = "Reporte"
        const val POINT_SALE= "Punto de venta"
        fun newInstance(listRoutes : List<TrackingInform>): InformRoutesMapDialogFragment {
            val frag = InformRoutesMapDialogFragment()
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
                    var v : View? = null
                    hmMarkerInfoWindow[p0.id]?.let {
                        if(it.typePoint == POINT_SALE){
                            v = layoutInflater.inflate(R.layout.info_adapter_pv_geolocation,null)
                            val tvSalesPoint= v?.findViewById<TextView>(R.id.tvSalesPointInfoAdapterPV)
                            val tvDirPv = v?.findViewById<TextView>(R.id.tvDirInfoAdapterPv)
                            val checkToDo = v?.findViewById<TextView>(R.id.tvCheckInToDoAdapterPv)
                            val checkFinished = v?.findViewById<TextView>(R.id.tvCheckInFinshiedAdapterPV)
                            val taskToDo = v?.findViewById<TextView>(R.id.tvTasksTodoAdapterPv)
                            val taskFinished = v?.findViewById<TextView>(R.id.tvTasksFinishedAdapterPv)
                            tvSalesPoint?.text = "${it.pv} - ${it.codPv}"
                            tvDirPv?.text = it.dirPv
                            checkToDo?.text = "Check out pendientes : ${it.checkInTodo}"
                            checkFinished?.text = "Check in realizados: ${it.checkInDone}"
                            taskToDo?.text = "Tareas pendientes : ${it.tasksTodo}"
                            taskFinished?.text = "Tareas realizadas : ${it.taskFinished}"

                        }else{
                            v = layoutInflater.inflate(R.layout.info_adapter_geolocation,null)
                            val tvLatLng = v?.findViewById<TextView>(R.id.tvDescInfoAdapter)
                            val tvSalesPoint= v?.findViewById<TextView>(R.id.tvSalesPointInfoAdapter)
                            val tvType = v?.findViewById<TextView>(R.id.tvTypeInfoAdapter)
                            tvType?.text = it.typePoint
                            tvSalesPoint?.text = "${it.pv} - ${it.codPv}"
                            tvLatLng?.text =  "("+p0.position.latitude.toString().substring(0,9)+","+p0.position.longitude.toString().substring(0,9)+")"
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
        listRoutes?.let { listInformTracking ->
            for (userTracking in listInformTracking) {
                Log.d("UserTracking", "UserTracking -> ${userTracking.nameUser} List -> ${userTracking.listTracking}")
                for (tracking in userTracking.listTracking) {
                    var checkin= false
                    var checkout= false
                    for (visit in tracking.visits) {
                        //for(visit in visits?.visit!!){
                        var markerId: String = ""
                        var markerType: String = ""
                        when (visit?.comment) {
                            "INGRESO" -> {
                                val userPosition =
                                    LatLng(visit.latitude.toDouble(), visit.longitude.toDouble())
                                val iconD = resources.getDrawable(R.drawable.ic_marker_checkin)
                                val marker = googleMap.addMarker(
                                    MarkerOptions()
                                        .position(userPosition)
                                        .infoWindowAnchor(0f, -0.1f)
                                        .icon(getMarkerIconFromDrawable(iconD))
                                )
                                checkin = true
                                markerId = marker.id
                                markerType = CHECK_IN
                                hashMapMarkerRoute!![marker.id] = InfoGeolocation(
                                    visit.comment,
                                    visit.creation,
                                    userTracking.nameUser,
                                    tracking.codPvCop,
                                    tracking.dirCorpPv,
                                    tracking.Pv
                                )
                            }
                            "SALIDA" -> {
                                val userPosition =
                                    LatLng(visit.latitude.toDouble(), visit.longitude.toDouble())
                                Log.d("pos", userPosition.toString())
                                val iconD = resources.getDrawable(R.drawable.ic_marker_checkout)
                                val marker = googleMap.addMarker(
                                    MarkerOptions()
                                        .position(userPosition)
                                        .icon(getMarkerIconFromDrawable(iconD))
                                )
                                checkout = true
                                markerId = marker.id
                                markerType = CHECK_OUT
                                hashMapMarkerRoute!![marker.id] = InfoGeolocation(
                                    visit.comment,
                                    visit.creation,
                                    userTracking.nameUser,
                                    tracking.codPvCop,
                                    tracking.dirCorpPv,
                                    tracking.Pv
                                )
                            }
                            "none" -> {
                                val userPosition =
                                    LatLng(visit.latitude.toDouble(), visit.longitude.toDouble())
                                Log.d("pos", userPosition.toString())
                                val iconD = resources.getDrawable(R.drawable.ic_marker_routes)
                                val marker = googleMap.addMarker(
                                    MarkerOptions()
                                        .position(userPosition)
                                        .icon(getMarkerIconFromDrawable(iconD))
                                )
                                markerType = POINT_SALE
                                hashMapMarkerRoute!![marker.id] = InfoGeolocation(
                                    visit.comment,
                                    visit.creation,
                                    userTracking.nameUser,
                                    tracking.codPvCop,
                                    tracking.dirCorpPv,
                                    tracking.Pv
                                )
                            }
                        }
                        hmMarkerInfoWindow[markerId] = InfoWindow(
                            tracking.Pv,
                            tracking.codPvCop,
                            tracking.dirCorpPv,
                            markerType,
                            tracking.checks.pendientes,
                            tracking.checks.concluidas,
                            tracking.reports.tareasPendientes,
                            tracking.reports.tareasCompletadas
                        )
                    }
                    for(task in tracking.tasks){
                        when(task?.reportState){
                            "COMPLETADO" -> {
                                val userPosition = LatLng(task.latitude.toDouble(), task.longitude.toDouble())
                                val iconD = resources.getDrawable(R.drawable.ic_marker_reports)
                                val marker = googleMap.addMarker(MarkerOptions()
                                    .position(userPosition)
                                    //.title(REPORTED)
                                    //.snippet("${tracking.Pv} - ${tracking.codPvCop}")
                                    .icon(getMarkerIconFromDrawable(iconD))
                                )
                                hmMarkerInfoWindow[marker.id] = InfoWindow(tracking.Pv, tracking.codPvCop,
                                    tracking.dirCorpPv,REPORTED, tracking.checks.pendientes,
                                    tracking.checks.concluidas, tracking.reports.tareasPendientes,
                                    tracking.reports.tareasCompletadas)
                                hashMapMarkerRoute!![marker.id] = InfoGeolocation("Reporte",task.creation, userTracking.nameUser,tracking.codPvCop,tracking.dirCorpPv,tracking.Pv)
                            }
                        }
                    }
                    if (firstTime) {
                        lat = tracking.latitude
                        long = tracking.longitude
                        firstTime = false
                    }
                    createMarkerPv(tracking,googleMap,checkin,checkout)
                }
            }
            if (lat != null && long != null) {
                Log.d("camara","entre")
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(lat!!, long!!)))
                googleMap.setMinZoomPreference(10f)
            }
        }
    }

    private fun createMarkerPv(
        tracking: Tracking,
        googleMap: GoogleMap,
        checkin: Boolean,
        checkout: Boolean
    ) {
        val state = if(checkout) "Terminado" else if (checkin) "Check In" else "En proceso"
        val pvPosition = LatLng(tracking.latitude!!,tracking.longitude!!)
        val iconD = resources.getDrawable(R.drawable.ic_marker_routes)
        lastLatLng = convertLatLngInMeter(pvPosition)
        val marker = googleMap.addMarker(MarkerOptions()
            .position(lastLatLng)
            .infoWindowAnchor(0f,-0.1f)
            .icon(getMarkerIconFromDrawable(iconD))
        )
        hmMarkerInfoWindow[marker.id] = InfoWindow(tracking.Pv, tracking.codPvCop,
            tracking.dirCorpPv, POINT_SALE, tracking.checks.pendientes,
            tracking.checks.concluidas, tracking.reports.tareasPendientes,
            tracking.reports.tareasCompletadas)
        //hashMapMarkerRoute!![marker.id] = InfoGeolocation(POINT_SALE, "", tracking.nameUser, tracking.codPvCop,tracking.dirCorpPv,tracking.Pv)
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
            binding.btnCloseMapRoutes.setOnClickListener {
                dismiss()
            }
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
            val dialog = InfoGeolocationDialogFragment.newInstance(it)
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

data class InfoWindow(
    val pv:String,
    val codPv:String,
    val dirPv:String,
    val typePoint:String,
    val checkInTodo:Int,
    val checkInDone:Int,
    val tasksTodo:Int,
    val taskFinished:Int
)