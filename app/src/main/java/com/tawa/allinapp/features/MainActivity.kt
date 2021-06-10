package com.tawa.allinapp.features

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import com.tawa.allinapp.R
import com.tawa.allinapp.core.platform.BaseActivity
import com.tawa.allinapp.features.auth.ui.LoginFragment
import com.tawa.allinapp.features.movies.ui.MoviesFragment

class MainActivity : BaseActivity() {

    companion object {
        fun callingIntent(context: Context) = Intent(context, MainActivity::class.java)


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        theme.applyStyle(R.style.AppTheme, true)
        super.onCreate(savedInstanceState)
    }


    override fun showToolbar() = false



    override fun fragment() = LoginFragment()
}