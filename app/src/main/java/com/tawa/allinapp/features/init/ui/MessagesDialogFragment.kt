package com.tawa.allinapp.features.init.ui

import android.app.NotificationManager
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.NotificationCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tawa.allinapp.R
import com.tawa.allinapp.core.extensions.observe
import com.tawa.allinapp.core.extensions.viewModel
import com.tawa.allinapp.core.platform.BaseFragment
import com.tawa.allinapp.databinding.DialogMessagesBinding
import com.tawa.allinapp.features.init.InitViewModel
import com.tawa.allinapp.models.Notify
import java.util.*
import javax.inject.Inject


class MessagesDialogFragment
    @Inject constructor(
    private val baseFragment: BaseFragment
    )
    : BottomSheetDialogFragment() {
    private lateinit var binding: DialogMessagesBinding
    private  lateinit var initViewModel: InitViewModel
    @Inject lateinit var notifyAdapter: NotifyAdapter
    @Inject lateinit var notifyAdapterYesterday: NotifyAdapterYesterday
    var listener: Callback? = null
    val notify = mutableListOf<Notify>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        baseFragment.appComponent.inject(this)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DialogMessagesBinding.inflate(inflater)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        isCancelable = true
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvNotifyToday.layoutManager = LinearLayoutManager(context)
        binding.rvNotifyYesterday.layoutManager = LinearLayoutManager(context)
        arguments?.let { bundle ->
            //bundle.getInt(TITLE).let { binding.tvItem.text = context?.getString(it) }
        }
        binding.rvNotifyToday.adapter = notifyAdapter
        binding.rvNotifyYesterday.adapter = notifyAdapterYesterday
        initViewModel = viewModel(baseFragment.viewModelFactory){
            observe(successGetNotify,{it?.let{
                val cal = Calendar.getInstance()
                val today  = cal.toInstant()
                val comp = today.toString().substring(0,10)
                cal.add(Calendar.DATE, -1)
                val yesterday = cal.toInstant()
                val compYesterday = yesterday.toString().substring(0,10)
                notifyAdapter.setData(it.filter {date-> date.dateCreation?.substring(0,10)==comp }.sortedByDescending { it.dateCreation })
                notifyAdapterYesterday.setData(it.filter {date-> date.dateCreation?.substring(0,10)==compYesterday }.sortedByDescending { it.dateCreation })
               // notifyAdapterYesterday.setData(it)
            }})
        }
        initViewModel.getNotify()
        notifyAdapter.clickListener = {
            Toast.makeText(context,it.id,Toast.LENGTH_SHORT).show()
        }
        binding.btnBackMessages.setOnClickListener {
            dismiss()
        }
        binding.view16.setOnClickListener {
            binding.rvNotifyYesterday.isVisible = false
            binding.rvNotifyToday.isVisible = true
        }
        binding.view18.setOnClickListener {
            binding.rvNotifyToday.isVisible = false
            binding.rvNotifyYesterday.isVisible = true

        }
    }
    private fun NotificationManager.sendNotification(messageBody: String, applicationContext: Context,channel:String) {
        val builder = NotificationCompat.Builder(
            applicationContext,
            channel
        )
            .setLargeIcon(BitmapFactory.decodeResource(requireContext().resources, R.mipmap.ic_launcher))
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentText(messageBody)
        notify(0, builder.build())
    }


    override fun onResume() {
        super.onResume()
        val params = dialog!!.window!!.attributes
        params.width = ConstraintLayout.LayoutParams.MATCH_PARENT
        dialog!!.window!!.attributes = params as android.view.WindowManager.LayoutParams
    }

    interface Callback {
      //  fun onAccept()
      //  fun onSendPassword()
    }
}