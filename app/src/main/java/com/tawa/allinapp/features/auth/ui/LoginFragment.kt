package com.tawa.allinapp.features.auth.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.tawa.allinapp.core.platform.BaseFragment
import com.tawa.allinapp.databinding.LoginFragmentBinding


class LoginFragment : BaseFragment() {

    private lateinit var binding:LoginFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = LoginFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.emailText.clearFocus()



        binding.button.setOnClickListener(View.OnClickListener {
            Toast.makeText(context,"Login",Toast.LENGTH_SHORT).show()
        })






       // mBinding.layoutPassword.setPasswordVisibilityToggleDrawable(R.drawable.ic_vi)

        //moviesViewModel.loadMovie(38001)

    }
}