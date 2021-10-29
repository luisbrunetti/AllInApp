package com.tawa.allinapp.features

import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.facebook.drawee.backends.pipeline.Fresco
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.tawa.allinapp.AndroidApplication
import com.tawa.allinapp.BuildConfig
import com.tawa.allinapp.R
import com.tawa.allinapp.core.di.ApplicationComponent
import com.tawa.allinapp.core.platform.TranslateObject
import com.tawa.allinapp.databinding.ActivityHomeBinding
import com.tawa.allinapp.features.init.InitViewModel
import javax.inject.Inject


class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var initViewModel: InitViewModel
    @Inject lateinit var translateObject: TranslateObject
    private val appComponent: ApplicationComponent by lazy(mode = LazyThreadSafetyMode.NONE) {
        (application as AndroidApplication).appComponent
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
        val dev = devMod()
        if(dev == BuildConfig.DEV) {
            Toast.makeText(applicationContext, "Error", Toast.LENGTH_LONG).show()
            return
        }
        Fresco.initialize(this)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_home)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_init,
                R.id.navigation_reports,
                R.id.navigation_pdv,
                R.id.navigation_routes,
                R.id.navigation_informs,
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        binding.navView.menu.findItem(R.id.navigation_informs).isVisible = false

        //binding.navView.menu.findItem(R.id.navigation_routes).isVisible = false

        changeViewLanguage()
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
    private fun changeViewLanguage(){
        binding.navView.menu.findItem(R.id.navigation_informs).title = translateObject.findTranslate("navigation_informs")
        binding.navView.menu.findItem(R.id.navigation_routes).title = translateObject.findTranslate("navigation_routes")
        binding.navView.menu.findItem(R.id.navigation_reports).title = translateObject.findTranslate("navigation_reports")
        binding.navView.menu.findItem(R.id.navigation_pdv).title = translateObject.findTranslate("navigation_pdv")
        binding.navView.menu.findItem(R.id.navigation_init).title = translateObject.findTranslate("navigation_init")
    }
    fun hideNavBar(){ binding.navView.visibility = View.GONE }

    fun showNavBar(){ binding.navView.visibility = View.VISIBLE }

    fun showInforms(){ binding.navView.menu.findItem(R.id.navigation_informs).isVisible = true}
    fun showRoutes(){ binding.navView.menu.findItem(R.id.navigation_routes).isVisible = true}

}