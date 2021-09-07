package com.tawa.allinapp.features.reports

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener
import com.tawa.allinapp.R
import com.tawa.allinapp.core.dialog.MessageDialogFragment
import com.tawa.allinapp.core.extensions.failure
import com.tawa.allinapp.core.extensions.loadFromResource
import com.tawa.allinapp.core.extensions.observe
import com.tawa.allinapp.core.extensions.viewModel
import com.tawa.allinapp.core.functional.Failure
import com.tawa.allinapp.core.platform.BaseFragment
import com.tawa.allinapp.databinding.FragmentReportsBinding
import com.tawa.allinapp.models.Report
import java.util.*
import javax.inject.Inject


class ReportsFragment : BaseFragment() {

    private lateinit var reportsViewModel: ReportsViewModel
    private lateinit var binding: FragmentReportsBinding
    private  var  typeUser = ""
    private  lateinit var  listReports:List<Report>
    @Inject lateinit var reportsAdapter: ReportsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkPermissions()
        binding.rvReports.layoutManager = LinearLayoutManager(context)
        binding.rvReports.adapter = reportsAdapter
        reportsAdapter.clickListener = {
            Log.d("reportname",it.reportName)
            when(it.reportName){
                /*"CHECK LIST PUNTO DE VENTA" -> findNavController().navigate(ReportsFragmentDirections.actionNavigationReportsToCheckListFragment(it.id))
                //"REPORTE FOTOGRAFICO" -> findNavController().navigate(ReportsFragmentDirections.actionNavigationReportsToPictureFragment())
               // "ESTATUS DE USUARIO" -> findNavController().navigate(ReportsFragmentDirections.actionNavigationReportsToUserStatusFragment())
                "LEVANTAMIENTO DE INFORMACIÓN" -> findNavController().navigate(ReportsFragmentDirections.actionNavigationReportsToCheckListFragment(it.id))*/
                "REPORTE DE AUDIOS" -> findNavController().navigate(ReportsFragmentDirections.actionNavigationReportsToAudioFragment(it.id))
                else->  findNavController().navigate(ReportsFragmentDirections.actionNavigationReportsToCheckListFragment(it.id,it.reportName))
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentReportsBinding.inflate(inflater)
        reportsViewModel = viewModel(viewModelFactory) {
            observe(reports, { it?.let {
                reportsAdapter.setData(it)
            } })
            observe(pvName, { it?.let {
                binding.tvHeaderPV.text = it
            } })
            observe(userType, { it?.let {
                if(it.isNotEmpty()) {
                    typeUser = it
                    reportsViewModel.getReports()
                }
            } })
            observe(successStateSku,{
                it?.let {
                    if(it.isNotEmpty()) {
                        binding.tvPVSub3.text = it
                        if(it=="En proceso") {
                           binding.iconSku.setImageResource(R.drawable.ic_inprocess)
                        }
                        if(it=="Enviado") {
                            binding.iconSku.setImageResource(R.drawable.ic_sended)
                        }
                    }
                }
            })
            observe(successStatePicture,{
                it?.let {
                    if(it.isNotEmpty())
                    {
                        binding.tvPhotoState.text = it
                        if(it=="En proceso")
                        {
                            binding.iconPicture.setImageResource(R.drawable.ic_inprocess)
                        }
                        if(it=="Enviado") {
                            binding.iconSku.setImageResource(R.drawable.ic_sended)
                        }
                    }
                }
            })
            observe(countSku,{it.let {
                if(it==1)
                {
                    binding.btnSku.isVisible  = true
                    binding.tvSku.isVisible = true
                    binding.iconSku.isVisible = true
                    binding.arrowSku.isVisible = true
                    reportsViewModel.getStateSku("")
                }

            }})
            failure(failure, { it?.let {
                hideProgressDialog()
                when(it){
                    is Failure.NetworkConnection    -> MessageDialogFragment.newInstance(getString(R.string.error_network)).show(childFragmentManager, "dialog")
                    is Failure.ServerError          -> MessageDialogFragment.newInstance(getString(R.string.error_network)).show(childFragmentManager, "dialog")
                    else                            -> MessageDialogFragment.newInstance(getString(R.string.error_unknown)).show(childFragmentManager, "dialog")
                }
            }})
        }
        reportsViewModel.getPVName()

        binding.etDate.setOnClickListener{
          getDay(binding.etDate)
        }
        binding.btnBackReports.setOnClickListener{
            activity?.onBackPressed()
        }
        binding.btnSku.setOnClickListener {
            findNavController().navigate(ReportsFragmentDirections.actionNavigationReportsToSkuFragment())
        }
        binding.btnReportPictures.setOnClickListener {
            findNavController().navigate(ReportsFragmentDirections.actionNavigationReportsToPictureFragment("${binding.tvPhotoState.text}"))
        }
        binding.appbar.addOnOffsetChangedListener(object : OnOffsetChangedListener {
            var isShow = true
            var scrollRange = -1
            override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.totalScrollRange
                }
                if (scrollRange + verticalOffset == 0) {
                    isShow = true
                    val animation: Animation =
                        AnimationUtils.loadAnimation(context, R.anim.scale_down)
                       binding.imageView5.startAnimation(animation)
                       binding.imageView5.setImageResource(R.drawable.ic_report_top_resize)
                } else if (isShow) {
                    val animation: Animation =
                        AnimationUtils.loadAnimation(context, R.anim.scale_up)
                    binding.imageView5.startAnimation(animation)
                    binding.imageView5.visibility = View.VISIBLE
                    binding.imageView5.visibility = View.VISIBLE
                    binding.imageView5.setImageResource(R.drawable.ic_report_top)
                    isShow = false
                }
            }
        })
        reportsViewModel.getCountSku()
        reportsViewModel.getStatePicture()
        return binding.root
    }

    private fun getDay(et: EditText){
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val dpd = DatePickerDialog(requireContext(), DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            et.setText("" + dayOfMonth + "/" +getMonth(monthOfYear) + "/" + year) }, year, month, day)
        dpd.show()
    }

    private fun getMonth(monthYear: Int) = when(monthYear){
            0 -> "01"
            1 -> "02"
            2 -> "03"
            3 -> "04"
            4 -> "05"
            5 -> "06"
            6 -> "07"
            7 -> "08"
            8 -> "09"
            9 -> "10"
            10 -> "11"
            11 -> "12"
        else  ->""
    }
}