package com.tawa.allinapp.features.splash

import com.tawa.allinapp.core.platform.BaseActivity

class SplashActivity : BaseActivity() {

    override fun showToolbar() = false

    override fun fragment() = SplashFragment()

}