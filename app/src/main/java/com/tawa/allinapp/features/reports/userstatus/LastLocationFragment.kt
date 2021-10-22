package com.tawa.allinapp.features.reports.userstatus

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.tawa.allinapp.R
import com.tawa.allinapp.core.platform.BaseFragment
import com.tawa.allinapp.databinding.FragmentLastLocationBinding



class LastLocationFragment : BaseFragment() {

    private lateinit var binding: FragmentLastLocationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentLastLocationBinding.inflate(inflater)
        binding.btnBackLocation.setOnClickListener{
            activity?.onBackPressed()
        }
        changeViewsFragment()
        return binding.root
    }

    override fun changeViewsFragment() {

    }

    private val mapCallback = OnMapReadyCallback { googleMap ->
        val userPosition = LatLng( arguments?.get("latitude").toString().toDouble(), arguments?.get("longitude").toString().toDouble())
        googleMap.addMarker(MarkerOptions().position(userPosition).title(arguments?.get("name").toString()))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(userPosition))
        googleMap.setMinZoomPreference(10f)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map_last) as SupportMapFragment?
        mapFragment?.getMapAsync(mapCallback)
    }

}