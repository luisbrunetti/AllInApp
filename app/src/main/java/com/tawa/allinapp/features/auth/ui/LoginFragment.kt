package com.tawa.allinapp.features.auth.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tawa.allinapp.R
import com.tawa.allinapp.core.extensions.failure
import com.tawa.allinapp.core.extensions.observe
import com.tawa.allinapp.core.extensions.viewModel
import com.tawa.allinapp.core.platform.BaseFragment
import com.tawa.allinapp.databinding.LoginFragmentBinding
import com.tawa.allinapp.features.auth.AuthViewModel


class LoginFragment : BaseFragment() {

    private lateinit var authViewModel: AuthViewModel
    private lateinit var binding:LoginFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = LoginFragmentBinding.inflate(inflater)
        authViewModel= viewModel(viewModelFactory) {
            observe(successLogin, { it?.let {
                if(it){
                    authViewModel.getCompaniesRemote()
                    authViewModel.getPVRemote()
                }
            }})
            observe(startLogin, { it?.let {
                if(it) showProgressDialog()
            }})
            observe(successGetCompanies, { it?.let {
                if(it) authViewModel.endLogin()
            }})
            observe(successGetPV, { it?.let {
                if(it) authViewModel.endLogin()
            }})
            observe(successEndLogin, { it?.let {
                if (it) {
                    hideProgressDialog()
                    showMessage(resources.getString(R.string.ok_login)) // TODO go to home
                }
            }})
            observe(username, { it?.let {
                authViewModel.validateFields()
            }})
            observe(password, { it?.let {
                authViewModel.validateFields()
            }})
            failure(failure, ::handleFailure)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpBinding()
    }

    private fun setUpBinding() {
        binding.viewModel = authViewModel
        binding.lifecycleOwner = this
        binding.executePendingBindings()
    }
}