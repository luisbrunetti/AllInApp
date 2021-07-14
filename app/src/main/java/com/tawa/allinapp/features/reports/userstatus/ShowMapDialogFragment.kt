package com.tawa.allinapp.features.reports.userstatus

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.tawa.allinapp.R
import com.tawa.allinapp.databinding.DialogShowMapBinding


class ShowMapDialogFragment: DialogFragment() {

    private lateinit var binding: DialogShowMapBinding
    var listener: Callback? = null
    var lat = ""
    var long = ""
    var nameGen= ""
    companion object {

        fun newInstance(latitudeG: String = "",longitudeG: String = "",nameG: String = ""): ShowMapDialogFragment {
            val frag = ShowMapDialogFragment()
            val bundle = Bundle()
            val data = ArrayList<String>()
            data.add(latitudeG)
            data.add(longitudeG)
            data.add(nameG)
            bundle.putStringArrayList("data", data)
            frag.arguments = bundle
            return frag
        }
    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DialogShowMapBinding.inflate(inflater)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        isCancelable = false
        return binding.root
    }

    private val mapCallback = OnMapReadyCallback { googleMap ->

        arguments?.let { bundle ->
            bundle.getStringArrayList("data")?.let {
                val userPosition = LatLng(it[0].toDouble(), it[1].toDouble())
                googleMap.addMarker(MarkerOptions().position(userPosition).title(it[2]))
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(userPosition))
                googleMap.setMinZoomPreference(10f)
            }
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map_dialog) as SupportMapFragment?
        mapFragment?.getMapAsync(mapCallback)
        binding.btnCloseMap.setOnClickListener{
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