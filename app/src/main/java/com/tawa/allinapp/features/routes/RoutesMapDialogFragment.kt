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
import com.tawa.allinapp.models.Routes


class RoutesMapDialogFragment: DialogFragment() {

    private lateinit var binding: DialogMapRoutesBinding
    var listener: Callback? = null
    var lat = ""
    var long = ""
    var nameGen= ""
    companion object {

        fun newInstance(listRoutes : List<Routes>): RoutesMapDialogFragment {
            val frag = RoutesMapDialogFragment()
            val bundle = Bundle()
            val arrayLatitude = ArrayList<String>()
            val arrayLongitude = ArrayList<String>()
            val arrayDirections = ArrayList<String>()
            for(route in listRoutes)
            {
                arrayLatitude.add(route.latitude.toString())
                arrayLongitude.add(route.longitude.toString())
                arrayDirections.add(route.dirCorpPv.toString())
            }
            bundle.putStringArrayList("latitude", arrayLatitude)
            bundle.putStringArrayList("longitude", arrayLongitude)
            bundle.putStringArrayList("directions", arrayDirections)
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
                        for((index,data ) in lat.withIndex())
                        {
                            val userPosition = LatLng(data.toDouble(), lon[index].toDouble())
                            Log.d("pos",userPosition.toString())
                            val iconD = resources.getDrawable(com.tawa.allinapp.R.drawable.ic_marker_routes)
                            googleMap.addMarker(MarkerOptions()
                                .position(userPosition).title(dir[index])
                                .icon(getMarkerIconFromDrawable(iconD))
                            )
                        }
                        googleMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(lat[0].toDouble(), lon[0].toDouble())))
                        googleMap.setMinZoomPreference(10f)


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