package com.tawa.allinapp.features.reports.geolocation.ui

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.compose.ui.text.toLowerCase
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import com.google.gson.Gson
import com.tawa.allinapp.core.platform.BaseFragment
import com.tawa.allinapp.databinding.FragmentInfoGeolocationDialogBinding
import com.tawa.allinapp.features.reports.geolocation.Constants
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject


class InfoGeolocationDialogFragment
@Inject constructor(val baseFragment: BaseFragment ) : DialogFragment() {
    private lateinit var binding: FragmentInfoGeolocationDialogBinding

    val params = LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.FILL_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    )

    companion object {
        const val INFO_GEOLOCATION = "info_geolocation"
        const val GENERIC_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
        const val FORMAT_VIEW = "dd-MM-yyy HH:mm:ss"
        fun newInstance(baseFragment: BaseFragment, infoGeolocation: InfoGeolocation) : InfoGeolocationDialogFragment {
            val frag = InfoGeolocationDialogFragment(baseFragment)
            val bundle = Bundle()
            val jsonObject = Gson().toJson(infoGeolocation)
            Log.d("object", infoGeolocation.toString())
            bundle.putString(INFO_GEOLOCATION, jsonObject)
            frag.arguments = bundle
            return frag
        }

    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInfoGeolocationDialogBinding.inflate(inflater)
        var infoGeolocation: InfoGeolocation?
        arguments?.getString(INFO_GEOLOCATION).let {
            val objectParsed = Gson().fromJson(it, InfoGeolocation::class.java)
            infoGeolocation = objectParsed
        }
        infoGeolocation?.let {
            val (date, time ) = parseTime(it.time)
            binding.tvUserInfoGeolocation.text = it.user
            binding.tvTypeInfoGeolocation.text = when(it.type.uppercase()){
                Constants.CHECK_IN.uppercase() -> baseFragment.translateObject.findTranslate("tvCheckInInfoGelocation") ?: "Ingreso"
                Constants.CHECK_OUT.uppercase() -> baseFragment.translateObject.findTranslate("tvCheckOutInfoGelocation") ?: "Salida"
                Constants.REPORT_COMPLETED.uppercase() -> baseFragment.translateObject.findTranslate("tvReportInfoGelocation") ?: "Reporte"
                else -> ""
            }
            binding.tvPointInfoGeolocation.text = "${it.pv_desc} - ${it.pv_cod}"
            binding.tvPointDirInfoGeolocation.text = it.pv_dir
            binding.tvDateInfoGeolocation.text = "$date - $time"
        }
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        binding.btnCloseMapRoutes.setOnClickListener { dismiss() }
        changeViewFragment()
        return binding.root
    }
    override fun onResume() {
        super.onResume()
        val params = dialog!!.window!!.attributes
        params.width = ConstraintLayout.LayoutParams.MATCH_PARENT
        dialog!!.window!!.attributes = params as android.view.WindowManager.LayoutParams
    }

    private fun changeViewFragment(){
        baseFragment.translateObject.apply {
            if(getInstance().isNotEmpty()){
                binding.tvTitleInfoGeolocation.text = findTranslate("tvTitleInfoGeolocation")  ?: "Información"
                binding.tvTypeTitleInfoGeolocation.text = findTranslate("tvTypeTitleInfoGeolocation") ?: "Tipo de geolocalización"
                binding.tvTitleUserInfoGeolocation.text = findTranslate("tvTitleUserInfoGeolocation") ?: "Usuario"
                binding.tvTitleDateInfoGeolocation.text = findTranslate("tvTitleDateInfoGeolocation") ?: "Fecha y hora"
                binding.tvTitlePointInfoGeolocation.text = findTranslate("tvTitlePointInfoGeolocation") ?: "Puntos de venta"
            }else baseFragment.authViewModel.getTranslate()
        }
    }

    private fun parseTime(time:String): Pair<String,String>{
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