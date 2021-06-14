package com.tawa.allinapp.features.auth.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.tawa.allinapp.AndroidApplication
import com.tawa.allinapp.R
import com.tawa.allinapp.core.di.ApplicationComponent
import com.tawa.allinapp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding:ActivityLoginBinding
    lateinit var receiver: BroadcastReceiver

    private val appComponent: ApplicationComponent by lazy(mode = LazyThreadSafetyMode.NONE) {
        (application as AndroidApplication).appComponent
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        appComponent.inject(this)
        val dev = devMod()

        if(dev==0) {
            Toast.makeText(applicationContext, "Error", Toast.LENGTH_LONG).show()
            return
        }

        binding = ActivityLoginBinding.inflate(layoutInflater)
        theme.applyStyle(R.style.AppTheme,true)
        getSupportActionBar()?.hide()


        setContentView(binding.root)



        findNavController(R.id.nav_host_auth_home)
    }

    fun devMod():Int{

        val devMode: Int = Settings.Secure.getInt(
            this.contentResolver,
            Settings.Global.DEVELOPMENT_SETTINGS_ENABLED, 0
        )

        return devMode

    }


    override fun onResume() {
       // Toast.makeText(applicationContext,"resumen",Toast.LENGTH_LONG).show()
        val dev = devMod()


        if(dev==0) finish()
        super.onResume()
    }
}

