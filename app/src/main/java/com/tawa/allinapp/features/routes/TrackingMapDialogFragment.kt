package com.tawa.allinapp.features.routes

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
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.tawa.allinapp.R
import com.tawa.allinapp.databinding.DialogMapRoutesBinding
import com.tawa.allinapp.models.InfoWindowRoutesData
import com.tawa.allinapp.models.InfoWindowTrackingData
import com.tawa.allinapp.models.Tracking


class TrackingMapDialogFragment: DialogFragment() {

    private lateinit var binding: DialogMapRoutesBinding
    var listener: Callback? = null
    var lat = ""
    var long = ""
    var nameGen= ""
    private var hashPvds : MutableMap<Marker, Pdvs> = mutableMapOf()
    companion object {

        fun newInstance(listTracking : List<Tracking>): TrackingMapDialogFragment {
            val frag = TrackingMapDialogFragment()
            val bundle = Bundle()
            val arrayLatitude = ArrayList<String>()
            val arrayLongitude = ArrayList<String>()
            val arrayDirections = ArrayList<String>()
            val arrayNamePdv = ArrayList<String>()
            val arrayType = ArrayList<String>()
            val arrayCheckInPending = ArrayList<String>()
            val arrayCheckOutPending = ArrayList<String>()
            val arrayCheckInComplete = ArrayList<String>()
            val arrayCheckOutComplete = ArrayList<String>()
            val arrayTaskPending = ArrayList<String>()
            val arrayTaskComplete = ArrayList<String>()
            for(track in listTracking)
            {
                Log.d("visitias",track.visits.toString())
                val visits = track.visits
                val tasks = track.tasks
                Log.d("tarea",tasks.toString())
                arrayLatitude.add(track.latitude.toString())
                arrayLongitude.add(track.longitude.toString())
                arrayDirections.add(track.dirCorpPv)
                arrayNamePdv.add(track.nameCorpPv)
                arrayCheckInPending.add(track.checks.checkIn.pendientes.toString())
                arrayCheckInComplete.add(track.checks.checkIn.concluidas.toString())
                arrayCheckOutPending.add(track.checks.checkOut.pendientes.toString())
                arrayCheckOutComplete.add(track.checks.checkOut.concluidas.toString())
                arrayTaskPending.add(track.reports.tareasPendientes.toString())
                arrayTaskComplete.add(track.reports.tareasCompletadas.toString())
                arrayType.add("none")

                if(!visits.isNullOrEmpty())
                {
                    for(visit in visits)
                    {
                        arrayLatitude.add(visit?.latitude.toString())
                        arrayLongitude.add(visit?.longitude.toString())
                        arrayDirections.add(track.dirCorpPv)
                        arrayNamePdv.add(track.nameCorpPv)
                        arrayType.add(visit?.comment.toString())
                        arrayCheckInPending.add(track.checks.checkIn.pendientes.toString())
                        arrayCheckInComplete.add(track.checks.checkIn.concluidas.toString())
                        arrayCheckOutPending.add(track.checks.checkOut.pendientes.toString())
                        arrayCheckOutComplete.add(track.checks.checkOut.concluidas.toString())
                        arrayTaskPending.add(track.reports.tareasPendientes.toString())
                        arrayTaskComplete.add(track.reports.tareasCompletadas.toString())
                    }
                    // Log.d("visitasDato",track.visits.toString())
                    if(!tasks.isNullOrEmpty())
                    {
                        Log.d("tareas",tasks.toString())
                        for(task in tasks)
                        {
                            arrayLatitude.add(task?.latitude.toString())
                            arrayLongitude.add(task?.longitude.toString())
                            arrayDirections.add(track.dirCorpPv)
                            arrayNamePdv.add(track.nameCorpPv)
                            arrayType.add(task?.reportState.toString())
                            arrayCheckInPending.add(track.checks.checkIn.pendientes.toString())
                            arrayCheckInComplete.add(track.checks.checkIn.concluidas.toString())
                            arrayCheckOutPending.add(track.checks.checkOut.pendientes.toString())
                            arrayCheckOutComplete.add(track.checks.checkOut.concluidas.toString())
                            arrayTaskPending.add(track.reports.tareasPendientes.toString())
                            arrayTaskComplete.add(track.reports.tareasCompletadas.toString())
                        }
                    }
                }

            }
            bundle.putStringArrayList("latitude", arrayLatitude)
            bundle.putStringArrayList("longitude", arrayLongitude)
            bundle.putStringArrayList("directions", arrayDirections)
            bundle.putStringArrayList("type", arrayType)
            bundle.putStringArrayList("namePdv", arrayNamePdv)
            bundle.putStringArrayList("checkInPending", arrayCheckInPending)
            bundle.putStringArrayList("checkInComplete", arrayCheckInComplete)
            bundle.putStringArrayList("checkOutPending", arrayCheckOutPending)
            bundle.putStringArrayList("checkOutComplete", arrayCheckOutComplete)
            bundle.putStringArrayList("taskPending", arrayTaskPending)
            bundle.putStringArrayList("taskComplete", arrayTaskComplete)
            frag.arguments = bundle
            return frag
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DialogMapRoutesBinding.inflate(inflater)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        isCancelable = false
        return binding.root
    }

    private val mapCallback = OnMapReadyCallback { googleMap ->


        arguments?.let { bundle ->
            bundle.getStringArrayList("latitude")?.let { lat->
                bundle.getStringArrayList("longitude")?.let { lon->
                    bundle.getStringArrayList("directions")?.let {dir->
                        bundle.getStringArrayList("type")?.let { type->
                            val namePdv = bundle.getStringArrayList("namePdv")!!
                            val checkInPending = bundle.getStringArrayList("checkInPending")
                            val checkOutPending = bundle.getStringArrayList("checkOutPending")
                            val checkInComplete = bundle.getStringArrayList("checkInComplete")
                            val checkOutComplete = bundle.getStringArrayList("checkOutComplete")
                            val taskPending = bundle.getStringArrayList("taskPending")
                            val taskComplete = bundle.getStringArrayList("taskComplete")

                            for((index,data ) in lat.withIndex())
                            {
                                if(type[index]=="INGRESO")
                                {
                                    val info = InfoWindowTrackingData(namePdv.get(index),"${data}, ${lon[index]}","Check in","","","","")
                                    val customInfoWindow = InfoWindowTracking(requireContext())
                                    googleMap.setInfoWindowAdapter(customInfoWindow)
                                    val userPosition = LatLng(data.toDouble(), lon[index].toDouble())
                                    Log.d("pos",userPosition.toString())
                                    val iconD = resources.getDrawable(com.tawa.allinapp.R.drawable.ic_marker_checkin)
                                    val marker = googleMap.addMarker(MarkerOptions()
                                        .position(userPosition).title("Check in "+dir[index])
                                        .snippet( " ("+data+","+lon[index]+")")
                                        .icon(getMarkerIconFromDrawable(iconD))
                                    )
                                    marker!!.tag = info
                                }
                                if(type[index]=="SALIDA")
                                {
                                    val info = InfoWindowTrackingData(namePdv.get(index),"${data}, ${lon[index]}","Check out","","","","")
                                    val customInfoWindow = InfoWindowTracking(requireContext())
                                    googleMap.setInfoWindowAdapter(customInfoWindow)
                                    val userPosition = LatLng(data.toDouble(), lon[index].toDouble())
                                    Log.d("pos",userPosition.toString())
                                    val iconD = resources.getDrawable(com.tawa.allinapp.R.drawable.ic_marker_checkout)
                                    val marker =  googleMap.addMarker(MarkerOptions()
                                        .position(userPosition).title("Check out "+dir[index])
                                        .snippet( " ("+data+","+lon[index]+")")
                                        .icon(getMarkerIconFromDrawable(iconD))
                                    )
                                    marker!!.tag = info
                                }
                                if(type[index]=="COMPLETADO")
                                {
                                    val info = InfoWindowTrackingData(namePdv.get(index),"${data}, ${lon[index]}","Reporte","","","","")
                                    val customInfoWindow = InfoWindowTracking(requireContext())
                                    googleMap.setInfoWindowAdapter(customInfoWindow)
                                    val userPosition = LatLng(data.toDouble(), lon[index].toDouble())
                                    Log.d("pos",userPosition.toString())
                                    val iconD = resources.getDrawable(com.tawa.allinapp.R.drawable.ic_marker_reports)
                                    val marker =  googleMap.addMarker(MarkerOptions()
                                        .position(userPosition).title("Reporte "+dir[index])
                                        .snippet( " ("+data+","+lon[index]+")")
                                        .icon(getMarkerIconFromDrawable(iconD))
                                    )
                                    marker!!.tag = info
                                }
                                if(type[index]=="none")
                                {
                                    val info = InfoWindowTrackingData(namePdv.get(index),dir[index],"","Check In pendientes/completados: "+checkInPending!![index]+"/"+checkInComplete!![index],"Check Out pendientes/completados: "+checkOutPending!![index]+"/"+checkOutComplete!![index],"Tareas pendientes: "+taskPending!![index],"Tareas completadas: "+taskComplete!![index])
                                    val customInfoWindow = InfoWindowTracking(requireContext())
                                    googleMap.setInfoWindowAdapter(customInfoWindow)
                                    val userPosition = LatLng(data.toDouble(), lon[index].toDouble())
                                    Log.d("pos",userPosition.toString())
                                    val iconD = resources.getDrawable(com.tawa.allinapp.R.drawable.ic_marker_routes)
                                    val marker = googleMap.addMarker(MarkerOptions()
                                        .position(userPosition).title(dir[index])
                                        .icon(getMarkerIconFromDrawable(iconD))
                                    )
                                    marker!!.tag = info
                                    hashPvds[marker] = Pdvs(checkInPending[index].toInt(), checkOutPending[index].toInt(),taskPending[index].toInt())
                                }
                            }
                            googleMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(lat[0].toDouble(), lon[0].toDouble())))
                            googleMap.setMinZoomPreference(10f)
                        }
                    }
                }
            }
        }
        checkPdvIsFinished()
    }

    private fun checkPdvIsFinished(){
        for(mark in hashPvds.keys){
            val pdv = hashPvds[mark]
            pdv?.let {
                if((pdv.checkInTodo == 0) &&
                    (pdv.checkOutToDo == 0) &&
                    (pdv.TasksToDo == 0)) {
                    mark.setIcon(
                        ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_marker_routes_finished
                    )?.let { it1 ->
                        getMarkerIconFromDrawable(
                            it1
                        )
                    })
                }
            }
        }
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
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(com.tawa.allinapp.R.id.map_dialog_routes) as SupportMapFragment?
        mapFragment?.getMapAsync(mapCallback)
        binding.btnCloseMapRoutes.setOnClickListener {
            dismiss()
        }
    }

    override fun onResume() {
        super.onResume()
        val params = dialog!!.window!!.attributes
        params.width = ConstraintLayout.LayoutParams.MATCH_PARENT
        dialog!!.window!!.attributes = params as android.view.WindowManager.LayoutParams
    }

    interface Callback {
        fun onAccept()
    }
    data class Pdvs(
        val checkInTodo: Int,
        val checkOutToDo :Int,
        val TasksToDo:Int
    )
}