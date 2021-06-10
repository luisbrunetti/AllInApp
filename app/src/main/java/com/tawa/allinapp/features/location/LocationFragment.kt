package com.tawa.allinapp.features.location

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
import com.tawa.allinapp.databinding.FragmentLocationBinding

class LocationFragment : BaseFragment() {

    private lateinit var _binding:FragmentLocationBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentLocationBinding.inflate(inflater)
        return _binding.root
    }

    private val mapCallback = OnMapReadyCallback { googleMap ->
        val sydney = LatLng(-34.0, 151.0)
        googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(mapCallback)
    }
}