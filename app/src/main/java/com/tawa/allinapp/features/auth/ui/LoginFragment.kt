package com.tawa.allinapp.features.auth.ui

import android.graphics.drawable.Drawable
import android.graphics.drawable.shapes.Shape
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tawa.allinapp.R
import com.tawa.allinapp.core.extensions.failure
import com.tawa.allinapp.core.extensions.observe
import com.tawa.allinapp.core.extensions.viewModel
import com.tawa.allinapp.core.platform.BaseFragment
import com.tawa.allinapp.databinding.FragmentMoviesBinding
import com.tawa.allinapp.databinding.LoginFragmentBinding
import com.tawa.allinapp.features.movies.MoviesViewModel
import kotlinx.android.synthetic.main.login_fragment.*
import javax.inject.Inject


class LoginFragment : BaseFragment() {



    private lateinit var mBinding:LoginFragmentBinding

    override fun layoutId() = R.layout.login_fragment

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        mBinding = LoginFragmentBinding.inflate(inflater)
        return mBinding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)


        appComponent.inject(this)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        mBinding.emailText.clearFocus()

        mBinding.button.setOnClickListener(View.OnClickListener {

            Toast.makeText(context,"Login",Toast.LENGTH_SHORT).show()

        })


       // mBinding.layoutPassword.setPasswordVisibilityToggleDrawable(R.drawable.ic_vi)

        //moviesViewModel.loadMovie(38001)

    }
}