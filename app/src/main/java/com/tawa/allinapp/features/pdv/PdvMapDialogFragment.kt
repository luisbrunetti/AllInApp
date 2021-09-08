package com.tawa.allinapp.features.pdv

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
import com.tawa.allinapp.databinding.DialogMapPdvBinding
import com.tawa.allinapp.databinding.DialogMapRoutesBinding
import com.tawa.allinapp.models.Routes


class PdvMapDialogFragment: DialogFragment() {

    private lateinit var binding: DialogMapPdvBinding
    var listener: Callback? = null
    var lat = ""
    var long = ""
    var nameGen= ""
    companion object {

        fun newInstance(latitude:String,longitude:String,name:String): PdvMapDialogFragment {
            val frag = PdvMapDialogFragment()
            val bundle = Bundle()
            bundle.putString("latitude", latitude)
            bundle.putString("longitude", longitude)
            bundle.putString("name", name)
            frag.arguments = bundle
            return frag
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DialogMapPdvBinding.inflate(inflater)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        isCancelable = false
        return binding.root
    }

    private val mapCallback = OnMapReadyCallback { googleMap ->
        arguments?.let { bundle ->
            bundle.getString("latitude")?.let { lat->
                bundle.getString("longitude")?.let { lon->
                    bundle.getString("name")?.let {
                        val userPosition = LatLng(lat.toDouble(), lon.toDouble())
                        googleMap.addMarker(MarkerOptions().position(userPosition).title(it))
                        googleMap.moveCamera(CameraUpdateFactory.newLatLng(userPosition))
                        googleMap.setMinZoomPreference(10f)
                    }
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
        binding.btnCloseMapPdv.setOnClickListener {
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