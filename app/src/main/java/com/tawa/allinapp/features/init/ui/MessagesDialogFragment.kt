package com.tawa.allinapp.features.init.ui

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tawa.allinapp.R
import com.tawa.allinapp.databinding.DialogMessagesBinding
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.tawa.allinapp.core.platform.BaseFragment
import com.tawa.allinapp.models.Notify
import javax.inject.Inject


class MessagesDialogFragment
    @Inject constructor(
    private val baseFragment: BaseFragment
    )
    : BottomSheetDialogFragment() {
    private lateinit var binding: DialogMessagesBinding
    @Inject lateinit var notifyAdapter: NotifyAdapter
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
        binding.rvNotify.layoutManager = LinearLayoutManager(context)
        arguments?.let { bundle ->
            //bundle.getInt(TITLE).let { binding.tvItem.text = context?.getString(it) }
        }
        notifyAdapter.clickListener = {
            Toast.makeText(context,it.id,Toast.LENGTH_SHORT).show()
        }
        notify.add(Notify("1","Prueba1","AB"))
        notify.add(Notify("2","Prueba2","FC"))
        notify.add(Notify("3","Prueba3","GV"))
        notify.add(Notify("4","Prueba4","RT"))
        notify.add(Notify("5","Prueba5","OW"))
        notify.add(Notify("6","Prueba6","HF"))
        notifyAdapter.setData(notify.sortedByDescending { it.id })
        binding.rvNotify.adapter = notifyAdapter

        binding.btnBackMessages.setOnClickListener {
            dismiss()
        }
        binding.btnNotfication.setOnClickListener {
            val channels  = "ch1"
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val name = "ch2"
                val descriptionText = "descrip"
                val importance = NotificationManager.IMPORTANCE_DEFAULT
                val channel = NotificationChannel(channels, name, importance).apply {
                    description = descriptionText
                }
                val nm = requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                nm.createNotificationChannel(channel)
                nm.sendNotification("Se agregaron nuevos puntos de venta",requireContext(),channels)
            }

            notify.add(Notify("7","Se ha cambiado el punto de venta para el usuario Alejandra Casas, Carlos Vargas, Francisco Rodríguez y Mario Perez ","RR"))
            notify.sortByDescending { it.id }
            notifyAdapter.setData(notify)
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