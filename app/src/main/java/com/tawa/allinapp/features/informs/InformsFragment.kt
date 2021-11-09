package com.tawa.allinapp.features.informs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.navigation.fragment.findNavController
import com.google.android.material.appbar.AppBarLayout
import com.tawa.allinapp.R
import com.tawa.allinapp.core.extensions.observe
import com.tawa.allinapp.core.extensions.viewModel
import com.tawa.allinapp.core.platform.BaseFragment
import com.tawa.allinapp.databinding.FragmentInformsBinding

import com.tawa.allinapp.databinding.FragmentPdvBinding
import com.tawa.allinapp.features.init.InitViewModel
import com.tawa.allinapp.features.reports.ReportsFragmentDirections

class InformsFragment : BaseFragment() {

    private lateinit var informsViewModel: InformsViewModel
    private lateinit var initViewModel: InitViewModel
    private lateinit var binding: FragmentInformsBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentInformsBinding.inflate(inflater)
        informsViewModel = viewModel(viewModelFactory) {
            observe(text, {
                it?.let {
                 //binding.tvInforms.text = it
                }
            })
        }

        binding.btnBackInforms.setOnClickListener {
            activity?.onBackPressed()
        }
        binding.btnStatusUser.setOnClickListener {
            findNavController().navigate(InformsFragmentDirections.actionNavigationInformsToUserStatusFragment())
        }
        binding.appbar.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
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
                    binding.imageView5.setImageResource(R.drawable.ic_informsicon_resize)
                } else if (isShow) {
                    val animation: Animation =
                        AnimationUtils.loadAnimation(context, R.anim.scale_up)
                    binding.imageView5.startAnimation(animation)
                    binding.imageView5.visibility = View.VISIBLE
                    binding.imageView5.visibility = View.VISIBLE
                    binding.imageView5.setImageResource(R.drawable.ic_informsicon)
                    isShow = false
                }
            }
        })
        binding.btDashboard.setOnClickListener {
            findNavController().navigate(InformsFragmentDirections.actionNavigationInformsToCoverageBoardFragment())
        }
        binding.btReportGeolocation.setOnClickListener {
            findNavController().navigate(InformsFragmentDirections.actionNavigationInformsToReportGeolocationFragment())
        }

        changeViewsFragment()

        return binding.root
    }
    override fun changeViewsFragment() {
        translateObject.apply {
            if (getInstance().isNotEmpty()) {
                binding.tvTitleInformsFragment.text = findTranslate("tvTitleInformsFragment")
                binding.tvSelectTypeInform.text = findTranslate("tvSelectTypeInform")
                binding.tvStatusUserInformFragment.text =
                    findTranslate("tvStatusUserInformFragment")
                binding.tvDashBoardInformFragment.text = findTranslate("tvDashBoardInformFragment")
                binding.tvReportGeolocation.text = findTranslate("tvGeolocationInformFragment")
            }else authViewModel.getTranslate()
        }
    }

}