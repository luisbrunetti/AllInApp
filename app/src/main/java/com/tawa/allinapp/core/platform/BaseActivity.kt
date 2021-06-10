package com.tawa.allinapp.core.platform

import android.os.Bundle
import android.view.View
import com.tawa.allinapp.R.id
import com.tawa.allinapp.R.layout
import androidx.appcompat.app.AppCompatActivity
import com.tawa.allinapp.core.extensions.inTransaction
import kotlinx.android.synthetic.main.toolbar.*

abstract class BaseActivity : AppCompatActivity() {

    abstract fun showToolbar(): Boolean

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_layout)
        if(showToolbar()){
            toolbar.visibility = View.GONE
            setSupportActionBar(toolbar)
        } else toolbar.visibility = View.GONE

        addFragment(savedInstanceState)
    }

    override fun onBackPressed() {
        (supportFragmentManager.findFragmentById(id.fragmentContainer) as BaseFragment).onBackPressed()
        super.onBackPressed()
    }

    private fun addFragment(savedInstanceState: Bundle?) =
        savedInstanceState ?: supportFragmentManager.inTransaction { add(id.fragmentContainer, fragment()) }

    abstract fun fragment(): BaseFragment
}