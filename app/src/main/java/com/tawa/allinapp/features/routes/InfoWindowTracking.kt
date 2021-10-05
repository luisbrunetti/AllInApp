package com.tawa.allinapp.features.routes

import android.app.Activity
import android.content.Context
import android.view.View
import androidx.core.view.isVisible
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.tawa.allinapp.R
import com.tawa.allinapp.models.InfoWindowTrackingData
import kotlinx.android.synthetic.main.dialog_tracking_info.view.*

class InfoWindowTracking(val context: Context) : GoogleMap.InfoWindowAdapter {

    override fun getInfoContents(p0: Marker?): View {

        var mInfoView = (context as Activity).layoutInflater.inflate(R.layout.dialog_tracking_info, null)
        var mInfoWindow: InfoWindowTrackingData? = p0?.tag as InfoWindowTrackingData?
        if(mInfoWindow!!.type.isEmpty())
            mInfoView.textView55.isVisible = false

        mInfoView.textView55.text = mInfoWindow.type
        mInfoView.textView56.text = mInfoWindow.content
        mInfoView.textView57.text = mInfoWindow.title
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