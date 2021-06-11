package com.tawa.allinapp.features.auth.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tawa.allinapp.core.extensions.failure
import com.tawa.allinapp.core.extensions.observe
import com.tawa.allinapp.core.extensions.viewModel
import com.tawa.allinapp.core.platform.BaseFragment
import com.tawa.allinapp.databinding.FragmentMoviesBinding
import com.tawa.allinapp.databinding.LoginFragmentBinding
import com.tawa.allinapp.features.auth.AuthViewModel
import com.tawa.allinapp.features.auth.usecase.DoLogin
import com.tawa.allinapp.features.movies.MoviesViewModel


class LoginFragment : BaseFragment() {

    private lateinit var authViewModel: AuthViewModel
    private lateinit var binding:LoginFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = LoginFragmentBinding.inflate(inflater)

        authViewModel= viewModel(viewModelFactory) {
            observe(success, {
                it?.let {

                }
            })
            failure(failure, {
                it?.let {
                    Toast.makeText(context,"prueba",Toast.LENGTH_SHORT).show()
                }
            })
        }

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

            Toast.makeText(context,"login",Toast.LENGTH_SHORT).show()
            //doLogin()
        })

    }

    fun doLogin(){

        authViewModel.login(
            "asas",
            "asas")



    }
}