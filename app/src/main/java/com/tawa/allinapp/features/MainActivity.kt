package com.tawa.allinapp.features

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import com.tawa.allinapp.R
import com.tawa.allinapp.core.platform.BaseActivity
import com.tawa.allinapp.features.auth.ui.LoginFragment


class MainActivity : BaseActivity() {

    companion object {
        fun callingIntent(context: Context) = Intent(context, MainActivity::class.java)


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        theme.applyStyle(R.style.AppTheme, true)
        getSupportActionBar()?.hide()


        super.onCreate(savedInstanceState)

    }


    override fun showToolbar() = false



    override fun fragment() = LoginFragment()
}