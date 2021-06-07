package com.tawa.allinapp.features

import android.content.Context
import android.content.Intent
import com.tawa.allinapp.core.platform.BaseActivity
import com.tawa.allinapp.features.movies.ui.MoviesFragment

class MainActivity : BaseActivity() {

    companion object {
        fun callingIntent(context: Context) = Intent(context, MainActivity::class.java)
    }

    override fun showToolbar() = false

    override fun fragment() = MoviesFragment()
}