package com.tawa.allinapp.features.reports.geolocation

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import com.google.gson.Gson
import com.tawa.allinapp.databinding.FragmentInfoGeolocationDialogBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class InfoGeolocationDialogFragment : DialogFragment() {
    private lateinit var binding: FragmentInfoGeolocationDialogBinding

    val params = LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.FILL_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    )

    companion object {
        const val INFO_GEOLOCATION ="info_geolocation"
        const val GENERIC_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
        const val FORMAT_VIEW = "dd-MM-yyy HH:mm:ss"
        fun newInstance(infoGeolocation: InfoGeolocation) : InfoGeolocationDialogFragment{
            val frag = InfoGeolocationDialogFragment()
            val bundle = Bundle()
            val jsonObject = Gson().toJson(infoGeolocation)
            Log.d("object", infoGeolocation.toString())
            bundle.putString(INFO_GEOLOCATION, jsonObject)
            frag.arguments = bundle
            return frag
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInfoGeolocationDialogBinding.inflate(inflater)
        var infoGeolocation: InfoGeolocation?
        arguments?.getString(INFO_GEOLOCATION).let {
            val objectParsed = Gson().fromJson(it,InfoGeolocation::class.java)
            infoGeolocation = objectParsed
        }
        infoGeolocation?.let {
            val (date, time ) = parseTime(it.time)
            binding.tvUserInfoGeolocation.text = it.user
            binding.tvTypeInfoGeolocation.text = it.type
            binding.tvPointInfoGeolocation.text = it.pv
            binding.tvDateInfoGeolocation.text = "$date - $time"
        }
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        binding.btnCloseMapRoutes.setOnClickListener {
            dismiss()
        }
        return binding.root
    }
    override fun onResume() {
        super.onResume()
        val params = dialog!!.window!!.attributes
        params.width = ConstraintLayout.LayoutParams.MATCH_PARENT
        dialog!!.window!!.attributes = params as android.view.WindowManager.LayoutParams
    }

    private fun parseTime(time:String): Pair<String,String>{
        Log.d("parse", time)
        val inputFormatter = DateTimeFormatter.ofPattern(GENERIC_FORMAT, Locale.ENGLISH)
        val outputFormatter = DateTimeFormatter.ofPattern(FORMAT_VIEW, Locale.ENGLISH)
        val dateTime = LocalDateTime.parse(time,inputFormatter)
        val formattedDate= outputFormatter.format(dateTime)
        val indexHour = formattedDate.indexOf(" ",0,true)
        val date = formattedDate.substring(0,indexHour)
        val time = formattedDate.substring(indexHour+1,formattedDate.length)
        return Pair(date,time)

    }
}