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
import androidx.fragment.app.DialogFragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.tawa.allinapp.databinding.DialogMapRoutesBinding
import com.tawa.allinapp.models.Tracking


class TrackingMapDialogFragment: DialogFragment() {

    private lateinit var binding: DialogMapRoutesBinding
    var listener: Callback? = null
    var lat = ""
    var long = ""
    var nameGen= ""
    companion object {

        fun newInstance(listTracking : List<Tracking>): TrackingMapDialogFragment {
            val frag = TrackingMapDialogFragment()
            val bundle = Bundle()
            val arrayLatitude = ArrayList<String>()
            val arrayLongitude = ArrayList<String>()
            val arrayDirections = ArrayList<String>()
            val arrayType = ArrayList<String>()
            for(track in listTracking)
            {
                Log.d("visitias",track.visits.toString())
                val visits = track.visits
                val tasks = track.tasks
                Log.d("tarea",tasks.toString())
                if(!visits.isNullOrEmpty())
                {
                    for(visit in visits)
                    {
                        arrayLatitude.add(visit?.latitude.toString())
                        arrayLongitude.add(visit?.longitude.toString())
                        arrayDirections.add(track.Pv+" - "+track.zoneName)
                        arrayType.add(visit?.comment.toString())
                    }
                   // Log.d("visitasDato",track.visits.toString())

                    if(!tasks.isNullOrEmpty())
                    {
                        Log.d("tareas",tasks.toString())
                        for(task in tasks)
                        {
                            arrayLatitude.add(task?.latitude.toString())
                            arrayLongitude.add(task?.longitude.toString())
                            arrayDirections.add(track.Pv+" - "+track.zoneName)
                            arrayType.add(task?.reportState.toString())
                        }
                    }

                }
                else
                {
                    arrayLatitude.add(track.latitude.toString())
                    arrayLongitude.add(track.longitude.toString())
                    arrayDirections.add(track.Pv+" - "+track.zoneName)
                    arrayType.add("none")
                }
            }
            bundle.putStringArrayList("latitude", arrayLatitude)
            bundle.putStringArrayList("longitude", arrayLongitude)
            bundle.putStringArrayList("directions", arrayDirections)
            bundle.putStringArrayList("type", arrayType)
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
                            for((index,data ) in lat.withIndex())
                            {
                                if(type[index]=="INGRESO")
                                {
                                    val userPosition = LatLng(data.toDouble(), lon[index].toDouble())
                                    Log.d("pos",userPosition.toString())
                                    val iconD = resources.getDrawable(com.tawa.allinapp.R.drawable.ic_marker_checkin)
                                    googleMap.addMarker(MarkerOptions()
                                        .position(userPosition).title("Check in "+dir[index])
                                        .snippet( " ("+data+","+lon[index]+")")
                                        .icon(getMarkerIconFromDrawable(iconD))
                                    )

                                }

                                if(type[index]=="SALIDA")
                                {
                                    val userPosition = LatLng(data.toDouble(), lon[index].toDouble())
                                    Log.d("pos",userPosition.toString())
                                    val iconD = resources.getDrawable(com.tawa.allinapp.R.drawable.ic_marker_checkout)
                                    googleMap.addMarker(MarkerOptions()
                                        .position(userPosition).title("Check out "+dir[index])
                                        .snippet( " ("+data+","+lon[index]+")")
                                        .icon(getMarkerIconFromDrawable(iconD))
                                    )

                                }

                                if(type[index]=="COMPLETADO")
                                {
                                    val userPosition = LatLng(data.toDouble(), lon[index].toDouble())
                                    Log.d("pos",userPosition.toString())
                                    val iconD = resources.getDrawable(com.tawa.allinapp.R.drawable.ic_marker_reports)
                                    googleMap.addMarker(MarkerOptions()
                                        .position(userPosition).title("Reporte "+dir[index])
                                        .snippet( " ("+data+","+lon[index]+")")
                                        .icon(getMarkerIconFromDrawable(iconD))
                                    )

                                }

                                if(type[index]=="none")
                                {
                                    val userPosition = LatLng(data.toDouble(), lon[index].toDouble())
                                    Log.d("pos",userPosition.toString())
                                    val iconD = resources.getDrawable(com.tawa.allinapp.R.drawable.ic_marker_routes)
                                    googleMap.addMarker(MarkerOptions()
                                        .position(userPosition).title(dir[index])
                                        .icon(getMarkerIconFromDrawable(iconD))
                                    )
                                }

                            }
                            googleMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(lat[0].toDouble(), lon[0].toDouble())))
                            googleMap.setMinZoomPreference(10f)

                        }


                    }
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
}