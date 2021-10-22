package com.tawa.allinapp.features.auth.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.tawa.allinapp.R
import com.tawa.allinapp.core.dialog.MessageDialogFragment
import com.tawa.allinapp.core.extensions.failure
import com.tawa.allinapp.core.extensions.observe
import com.tawa.allinapp.core.extensions.viewModel
import com.tawa.allinapp.core.functional.Failure
import com.tawa.allinapp.core.platform.BaseFragment
import com.tawa.allinapp.databinding.FragmentLoginBinding
import com.tawa.allinapp.features.auth.AuthViewModel
import java.io.IOException


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
                }}})
            observe(username, { it?.let {
                authViewModel.validateFields()
            }})
            observe(password, { it?.let {
                authViewModel.validateFields()
            }})
            observe(getLanguageSuccess, {
                it?.let {
                    if (translateObject.getInstance().arrayTranslate.isNotEmpty()) {
                        Log.d("logintest", translateObject.getInstance().arrayTranslate.toString())
                        //translateObject.setInstance(it)
                        //translate = it.arrayTranslate
                        if(translateObject.LANGUAGE==0) binding.swLoginFragment.text = "Espanol"
                        else binding.swLoginFragment.text = "English"
                    }
                }
            })
            observe(setLanguageSuccess,{
                if(it == true){
                    changeViewsFragment()
                    if(translateObject.LANGUAGE==0) binding.swLoginFragment.text = "Espanol"
                    else binding.swLoginFragment.text = "English"
                    hideProgressDialog()
                }
            })
            failure(failure, { it?.let {
                hideProgressDialog()
                when(it){
                    is Failure.DefaultError         -> authViewModel.setErrorLogin(it.message ?: getString(R.string.error_unknown))
                    is Failure.NetworkConnection    -> MessageDialogFragment.newInstance(getString(R.string.error_network)).show(childFragmentManager, "dialog")
                    is Failure.ServerError          -> MessageDialogFragment.newInstance(getString(R.string.error_network)).show(childFragmentManager, "dialog")
                    else                            -> MessageDialogFragment.newInstance(getString(R.string.error_unknown)).show(childFragmentManager, "dialog")
                }}})
        }
        binding.btnLoginFragment.setOnClickListener {
            authViewModel.doLogin()
            //showHome(context,true)
        }

        binding.cbRememberLoginFragment.setOnClickListener { authViewModel.setSession(binding.cbRememberLoginFragment.isChecked) }

        binding.edForgotPassword.setOnClickListener { findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToSendPasswordFragment2()) }

        binding.swLoginFragment.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                showProgressDialog()
                translateObject.LANGUAGE = 1
                //val listType = object : TypeToken<List<TranslateItem>>() {}.type
                //translate = Gson().fromJson(getJsonDataFromAsset(requireContext(),"mapText.json"),listType)
                authViewModel.setLanguage(1)
            }else{
                showProgressDialog()
                translateObject.LANGUAGE = 0
                authViewModel.setLanguage(1)
            }
        }
        authViewModel.getLanguage()
        return binding.root
    }

     override fun changeViewsFragment() {
        translateObject.apply {
            binding.tvHellowLoginFragment.text = findTranslate("tvHellowLoginFragment")
            binding.tvInfoLoginFragment.text = findTranslate("tvInfoLoginFragment")
            binding.txtInputUserLoginFragment.hint = findTranslate("txtInputUserLoginFragment")
            binding.txtInputPasswordLoginFragment.hint = findTranslate("txtInputPasswordLoginFragment")
            binding.cbRememberLoginFragment.text = findTranslate("cbRememberLoginFragment")
            binding.btnLoginFragment.text = findTranslate("btnLoginFragment")
            binding.edForgotPassword.text = findTranslate("edForgotPassword")
        }
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


    private fun checkLanguageNumber(){

    }

    fun getJsonDataFromAsset(context: Context, fileName: String): String? {
        val jsonString: String
        try {
            jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return null
        }
        return jsonString
    }
}