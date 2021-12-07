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
import com.tawa.allinapp.databinding.DialogMapRoutesBinding
import com.tawa.allinapp.models.Routes

import com.google.android.gms.maps.model.*
import com.tawa.allinapp.R
import com.tawa.allinapp.models.InfoWindowRoutesData


class RoutesMapDialogFragment: DialogFragment() {

    private lateinit var binding: DialogMapRoutesBinding
    var listener: Callback? = null
    var lat = ""
    var long = ""
    var nameGen= ""
    private var hashPvds : MutableMap<Marker,Pdvs> = mutableMapOf()

    companion object {

        fun newInstance(listRoutes : List<Routes>): RoutesMapDialogFragment {
            val frag = RoutesMapDialogFragment()
            val bundle = Bundle()
            val arrayLatitude = ArrayList<String>()
            val arrayLongitude = ArrayList<String>()
            val arrayDirections = ArrayList<String>()
            val arrayNamePdv = ArrayList<String>()
            val arrayCheckInPending = ArrayList<String>()
            val arrayCheckOutPending = ArrayList<String>()
            val arrayCheckInComplete = ArrayList<String>()
            val arrayCheckOutComplete = ArrayList<String>()
            val arrayTaskPending = ArrayList<String>()
            val arrayTaskComplete = ArrayList<String>()
            for(route in listRoutes)
            {
                arrayLatitude.add(route.latitude.toString())
                arrayLongitude.add(route.longitude.toString())
                arrayDirections.add(route.dirCorpPv.toString())
                arrayNamePdv.add(route.nameCorpPv.toString())
                arrayCheckInPending.add(route.checks.checkIn.pendientes.toString())
                arrayCheckInComplete.add(route.checks.checkIn.concluidas.toString())
                arrayCheckOutPending.add(route.checks.checkOut.pendientes.toString())
                arrayCheckOutComplete.add(route.checks.checkOut.concluidas.toString())
                arrayTaskPending.add(route.reports.tareasPendientes.toString())
                arrayTaskComplete.add(route.reports.tareasCompletadas.toString())

            }
            bundle.putStringArrayList("latitude", arrayLatitude)
            bundle.putStringArrayList("longitude", arrayLongitude)
            bundle.putStringArrayList("directions", arrayDirections)
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
        hashPvds = mutableMapOf()
        arguments?.let { bundle ->
            bundle.getStringArrayList("latitude")?.let { lat->
                bundle.getStringArrayList("longitude")?.let { lon->
                    bundle.getStringArrayList("directions")?.let {dir->
                        val name = bundle.getStringArrayList("namePdv")
                        val checkInPending = bundle.getStringArrayList("checkInPending")
                        val checkOutPending = bundle.getStringArrayList("checkOutPending")
                        val checkInComplete = bundle.getStringArrayList("checkInComplete")
                        val checkOutComplete = bundle.getStringArrayList("checkOutComplete")
                        val taskPending = bundle.getStringArrayList("taskPending")
                        val taskComplete = bundle.getStringArrayList("taskComplete")

                        for((index,data ) in lat.withIndex())
                        {
                            val info = InfoWindowRoutesData(name!![index], dir[index],"Check In pendientes/completados: "+checkInPending!![index]+"/"+checkInComplete!![index],"Check Out pendientes/completados: "+checkOutPending!![index]+"/"+checkOutComplete!![index],"Tareas pendientes: "+taskPending!![index],"Tareas completadas: "+taskComplete!![index])
                            val customInfoWindow = InfoWindowRoutes(requireContext())
                            googleMap.setInfoWindowAdapter(customInfoWindow)
                            val userPosition = LatLng(data.toDouble(), lon[index].toDouble())
                            Log.d("pos",userPosition.toString())
                            val iconD = resources.getDrawable(com.tawa.allinapp.R.drawable.ic_marker_routes)
                            val marker = googleMap.addMarker(MarkerOptions()
                                .position(userPosition).title(name?.get(index))
                                .snippet(dir[index])
                                .icon(getMarkerIconFromDrawable(iconD))
                            )
                            marker!!.tag = info
                            hashPvds[marker] = Pdvs(checkInPending[index].toInt(),
                                checkOutToDo = checkOutPending[index].toInt(),
                                taskPending[index].toInt())
                           // marker.showInfoWindow()
                        }
                        googleMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(lat[0].toDouble(), lon[0].toDouble())))
                        googleMap.setMinZoomPreference(10f)
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
                    mark.setIcon(ContextCompat.getDrawable(
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

    private fun getMarkerIconFromDrawable(drawable: Drawable): BitmapDescriptor? { val canvas = Canvas()
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