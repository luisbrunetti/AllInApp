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


class InformRoutesMapDialogFragment : DialogFragment(), GoogleMap.OnInfoWindowClickListener {

    private lateinit var binding: FragmentInformRoutesMapDialogBinding
    private var listRoutes: List<TrackingInform>? = null
    private var listener: Callback? = null
    private var listMarkers : ArrayList<Marker?>? = null
    private var hashMapMarkerRoute : MutableMap<String, InfoGeolocation>? = null

    companion object {
        const val LIST_ROUTES_INFORM = "list_routes_inform"
        
        fun newInstance(listRoutes : List<TrackingInform>): InformRoutesMapDialogFragment {
            val frag = InformRoutesMapDialogFragment()
            val bundle = Bundle()
           // val listType: Type = object : TypeToken<List<String?>?>() {}.type
            val jsonParsed = Gson().toJson(listRoutes)
            //Log.d("list",listRoutes.toString())
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
                    for(visit in tracking.visits){
                        when (visit?.comment) {
                            "INGRESO" -> {
                                val userPosition = LatLng(visit.latitude.toDouble(), visit.longitude.toDouble())
//                                Log.d("pos",userPosition.toString())
                                val iconD = resources.getDrawable(R.drawable.ic_marker_checkin)
                                val marker = googleMap.addMarker(MarkerOptions()
                                    .position(userPosition).title("Check in "+ tracking.Pv+ " - "+tracking.zoneName)
                                    .snippet( " ("+visit.latitude+","+visit.longitude+")")
                                    .icon(getMarkerIconFromDrawable(iconD))
                                )
                                hashMapMarkerRoute!![marker.id] = InfoGeolocation(visit.comment, visit.creation, userTracking.nameUser, tracking.codPvCop,tracking.dirCorpPv,tracking.Pv)
                            }
                            "SALIDA" -> {
                                val userPosition = LatLng(visit.latitude.toDouble(), visit.longitude.toDouble())
                                Log.d("pos",userPosition.toString())
                                val iconD = resources.getDrawable(R.drawable.ic_marker_checkout)
                                val marker = googleMap.addMarker(MarkerOptions()
                                    .position(userPosition).title("Check Out "+ tracking.Pv+ " - "+tracking.zoneName)
                                    .snippet( " ("+visit.latitude+","+visit.longitude+")")
                                    .icon(getMarkerIconFromDrawable(iconD))
                                )
                                hashMapMarkerRoute!![marker.id] = InfoGeolocation(visit.comment,visit.creation, userTracking.nameUser, tracking.codPvCop,tracking.dirCorpPv,tracking.Pv)
                            }
                            "COMPLETADO" -> {
                                val userPosition = LatLng(visit.latitude.toDouble(), visit.longitude.toDouble())
                                Log.d("pos",userPosition.toString())
                                val iconD = resources.getDrawable(R.drawable.ic_marker_reports)
                                val marker = googleMap.addMarker(MarkerOptions()
                                    .position(userPosition).title("Reporte"+ tracking.Pv+ " - "+tracking.zoneName)
                                    .snippet( " ("+visit.latitude+","+visit.longitude+")")
                                    .icon(getMarkerIconFromDrawable(iconD))
                                )
                                hashMapMarkerRoute!![marker.id] = InfoGeolocation(visit.comment,visit.creation, userTracking.nameUser,tracking.codPvCop,tracking.dirCorpPv,tracking.Pv)
                            }
                            "none" -> {
                                val userPosition = LatLng(visit.latitude.toDouble(), visit.longitude.toDouble())
                                Log.d("pos",userPosition.toString())
                                val iconD = resources.getDrawable(R.drawable.ic_marker_routes)
                                val marker = googleMap.addMarker(MarkerOptions()
                                    .position(userPosition).title(""+ tracking.Pv+ " - "+tracking.zoneName)
                                    .snippet( " ("+visit.latitude+","+visit.longitude+")")
                                    .icon(getMarkerIconFromDrawable(iconD))
                                )
                                hashMapMarkerRoute!![marker.id] = InfoGeolocation(visit.comment,visit.creation, userTracking.nameUser,tracking.codPvCop,tracking.dirCorpPv,tracking.Pv)
                            }
                        }
                    }
                    for(task in tracking.tasks){
                        when(task?.reportState){
                            "COMPLETADO" -> {
                                val userPosition = LatLng(task.latitude.toDouble(), task.longitude.toDouble())
                                Log.d("pos",userPosition.toString())
                                val iconD = resources.getDrawable(R.drawable.ic_marker_reports)
                                val marker = googleMap.addMarker(MarkerOptions()
                                    .position(userPosition).title("Reporte"+ tracking.Pv+ " - "+tracking.zoneName)
                                    .snippet( " ("+task.latitude+","+task.longitude+")")
                                    .icon(getMarkerIconFromDrawable(iconD))
                                )
                                hashMapMarkerRoute!![marker.id] = InfoGeolocation("Reporte",task.creation, userTracking.nameUser,tracking.codPvCop,tracking.dirCorpPv,tracking.Pv)
                            }
                        }
                    }
                    if (firstTime) {
                        lat = tracking.latitude
                        long = tracking.longitude
                        firstTime = false
                    }
                }
            }
            if (lat != null && long != null) {
                Log.d("camara","entre")
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(lat!!, long!!)))
                googleMap.setMinZoomPreference(10f)
            }
        }
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