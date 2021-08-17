package com.tawa.allinapp.features.init.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.google.android.material.appbar.AppBarLayout
import com.tawa.allinapp.R
import com.tawa.allinapp.core.extensions.failure
import com.tawa.allinapp.core.extensions.observe
import com.tawa.allinapp.core.extensions.viewModel
import com.tawa.allinapp.core.functional.Failure
import com.tawa.allinapp.core.platform.BaseFragment
import com.tawa.allinapp.databinding.FragmentInformsBinding

import com.tawa.allinapp.databinding.FragmentPdvBinding
import com.tawa.allinapp.databinding.FragmentSendPasswordBinding
import com.tawa.allinapp.features.init.InitViewModel
import com.tawa.allinapp.features.reports.ReportsFragmentDirections

class SendPasswordFragment : BaseFragment() {

    private lateinit var initViewModel: InitViewModel
    private lateinit var binding: FragmentSendPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSendPasswordBinding.inflate(inflater)
        initViewModel = viewModel(viewModelFactory) {
            observe(successSendPassword, {
                it?.let {
                    if(it)
                    {
                        showConfirmSendPassword()
                    }
                }
            })
            failure(failure,{
                when(it)
                {
                    is Failure.DefaultError -> {
                        binding.tvError.isVisible =true
                        binding.contePassword.setBackgroundResource(R.drawable.error_border)
                    }
                    else -> ""
                }
            })
        }
        binding.btnBackSendPass.setOnClickListener {
            activity?.onBackPressed()
        }
        binding.btnSendPass.setOnClickListener {
            binding.tvError.isVisible =false
            binding.contePassword.setBackgroundResource(R.drawable.selector)
            initViewModel.sendPassword(binding.edSendPass.text.toString())
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
                    binding.imageView5.setImageResource(R.drawable.ic_send_password_top_resize)
                } else if (isShow) {
                    val animation: Animation =
                        AnimationUtils.loadAnimation(context, R.anim.scale_up)
                    binding.imageView5.startAnimation(animation)
                    binding.imageView5.visibility = View.VISIBLE
                    binding.imageView5.visibility = View.VISIBLE
                    binding.imageView5.setImageResource(R.drawable.ic_send_password_top)
                    isShow = false
                }
            }
        })
        return binding.root
    }

    private fun showConfirmSendPassword(){
        val dialog = PasswordSendDialogFragment(this)
        dialog.listener = object : PasswordSendDialogFragment.Callback{
            override fun onClick() {
                activity?.onBackPressed()
            }
        }
        dialog.show(childFragmentManager,"")

    }

}