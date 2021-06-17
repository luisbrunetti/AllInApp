package com.tawa.allinapp.features.splash

import android.os.Bundle
import com.tawa.allinapp.R
import com.tawa.allinapp.core.platform.BaseActivity

class SplashActivity : BaseActivity() {

    override fun showToolbar() = false

    override fun fragment() = SplashFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        theme.applyStyle(R.style.AppTheme,true)
    }
}