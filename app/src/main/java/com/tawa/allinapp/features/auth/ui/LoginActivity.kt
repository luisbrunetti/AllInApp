package com.tawa.allinapp.features.auth.ui

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.tawa.allinapp.AndroidApplication
import com.tawa.allinapp.BuildConfig
import com.tawa.allinapp.R
import com.tawa.allinapp.core.di.ApplicationComponent
import com.tawa.allinapp.databinding.ActivityLoginBinding
import com.tawa.allinapp.features.HomeActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding:ActivityLoginBinding

    private val appComponent: ApplicationComponent by lazy(mode = LazyThreadSafetyMode.NONE) {
        (application as AndroidApplication).appComponent
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)

        Toast.makeText(applicationContext,"LoginActivity", Toast.LENGTH_SHORT).show()
        // antifake
        val dev = devMod()

        if(dev== BuildConfig.DEV) {
            Toast.makeText(applicationContext, "Error", Toast.LENGTH_LONG).show()
            return
        }

        binding = ActivityLoginBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        setContentView(binding.root)
        findNavController(R.id.nav_host_auth_home)
    }

    private fun devMod():Int{
        return Settings.Secure.getInt(
            this.contentResolver,
            Settings.Global.DEVELOPMENT_SETTINGS_ENABLED, 0
        )
    }

    override fun onResume() {
        super.onResume()
        val dev = devMod()
        if(dev==BuildConfig.DEV) finish()

    }
}

