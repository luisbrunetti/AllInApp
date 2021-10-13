package com.tawa.allinapp.features.auth.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import com.tawa.allinapp.R
import com.tawa.allinapp.core.dialog.MessageDialogFragment
import com.tawa.allinapp.core.extensions.failure
import com.tawa.allinapp.core.extensions.observe
import com.tawa.allinapp.core.extensions.viewModel
import com.tawa.allinapp.core.functional.Failure
import com.tawa.allinapp.core.platform.BaseFragment
import com.tawa.allinapp.databinding.FragmentLoginBinding
import com.tawa.allinapp.features.auth.AuthViewModel


class LoginFragment : BaseFragment() {

    private lateinit var authViewModel: AuthViewModel
    private lateinit var binding:FragmentLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentLoginBinding.inflate(inflater)
        authViewModel= viewModel(viewModelFactory) {
            observe(successLogin, { it?.let {
                if(it) authViewModel.getCompaniesRemote()
            }})
            observe(startLogin, { it?.let {
                if(it) showProgressDialog()
            }})
            observe(successGetCompanies, { it?.let {
                if(it) authViewModel.endLogin()
            }})
            observe(successEndLogin, { it?.let {
                if (it) {
                    hideProgressDialog()
                    showHome(context,true)
                }
            }})
            observe(username, { it?.let {
                authViewModel.validateFields()
            }})
            observe(password, { it?.let {
                authViewModel.validateFields()
            }})
            observe(successfulTranslate,{ it?.let {
                listLanguage = it
                changeLanguage(binding.root)
                hideProgressDialog()
                binding.swLoginFragment.text = "English"
            }})
            failure(failure, { it?.let {
                hideProgressDialog()
                when(it){
                    is Failure.DefaultError         -> authViewModel.setErrorLogin(it.message ?: getString(R.string.error_unknown))
                    is Failure.NetworkConnection    -> MessageDialogFragment.newInstance(getString(R.string.error_network)).show(childFragmentManager, "dialog")
                    is Failure.ServerError          -> MessageDialogFragment.newInstance(getString(R.string.error_network)).show(childFragmentManager, "dialog")
                    else                            -> MessageDialogFragment.newInstance(getString(R.string.error_unknown)).show(childFragmentManager, "dialog")
                }
            }})
        }

        binding.cbRememberLoginFragment.setOnClickListener {
            authViewModel.setSession(binding.cbRememberLoginFragment.isChecked)
        }
        binding.edForgotPassword.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToSendPasswordFragment2())
        }

        binding.swLoginFragment.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                showProgressDialog()
                CURRENT_LANGUAGE = ENGLISH
                authViewModel.getTranslate(ENGLISH)
                binding.swLoginFragment.text = "English"
            }else{
                showProgressDialog()
                CURRENT_LANGUAGE = SPANISH
                changeLanguage(binding.root)
                authViewModel.setLanguage(SPANISH)
                binding.swLoginFragment.text = "Español"
                hideProgressDialog()
            }
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