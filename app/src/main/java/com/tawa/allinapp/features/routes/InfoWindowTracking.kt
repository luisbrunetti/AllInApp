package com.tawa.allinapp.features.routes

import android.app.Activity
import android.content.Context
import android.view.View
import androidx.core.view.isVisible
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.tawa.allinapp.R
import com.tawa.allinapp.models.InfoWindowTrackingData
import kotlinx.android.synthetic.main.dialog_routes_info.view.*
import kotlinx.android.synthetic.main.dialog_tracking_info.view.*
import kotlinx.android.synthetic.main.dialog_tracking_info.view.textView55
import kotlinx.android.synthetic.main.dialog_tracking_info.view.textView56

class InfoWindowTracking(val context: Context) : GoogleMap.InfoWindowAdapter {

    override fun getInfoContents(p0: Marker?): View {

        var mInfoView = (context as Activity).layoutInflater.inflate(R.layout.dialog_tracking_info, null)
        var mInfoWindow: InfoWindowTrackingData? = p0?.tag as InfoWindowTrackingData?
        if(mInfoWindow!!.type.isEmpty())
            mInfoView.textView55.isVisible = false
        else
        {
            mInfoView.txVisitsPending2.isVisible = false
            mInfoView.txVisitsComplete2.isVisible = false
            mInfoView.txTaskPending2.isVisible = false
            mInfoView.txTaskComplete2.isVisible = false
        }

        mInfoView.textView55.text = mInfoWindow.type
        mInfoView.textView56.text = mInfoWindow.content
        mInfoView.textView57.text = mInfoWindow.title
        mInfoView.txVisitsPending2.text = mInfoWindow.visitsPending
        mInfoView.txVisitsComplete2.text = mInfoWindow.visitsComplete
        mInfoView.txTaskPending2.text = mInfoWindow.taskPending
        mInfoView.txTaskComplete2.text = mInfoWindow.taskComplete
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