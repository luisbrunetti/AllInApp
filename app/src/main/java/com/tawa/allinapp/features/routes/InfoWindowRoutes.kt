package com.tawa.allinapp.features.routes

import android.app.Activity
import android.content.Context
import android.view.View
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.tawa.allinapp.R
import com.tawa.allinapp.models.InfoWindowRoutesData
import kotlinx.android.synthetic.main.dialog_routes_info.view.*

class InfoWindowRoutes(val context: Context) : GoogleMap.InfoWindowAdapter {

    override fun getInfoContents(p0: Marker?): View {

        var mInfoView = (context as Activity).layoutInflater.inflate(R.layout.dialog_routes_info, null)
        var mInfoWindow: InfoWindowRoutesData? = p0?.tag as InfoWindowRoutesData?

        mInfoView.textView55.text = mInfoWindow?.title
        mInfoView.textView56.text = mInfoWindow?.content
        mInfoView.textView55.setOnClickListener {
            closeMarker(p0)
        }
        return mInfoView
    }

    private fun closeMarker(p0:Marker?){
        p0?.hideInfoWindow()
    }

    override fun getInfoWindow(p0: Marker?): View? {
        return null
    }
}