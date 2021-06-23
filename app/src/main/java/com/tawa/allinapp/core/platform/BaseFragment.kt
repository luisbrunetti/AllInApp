package com.tawa.allinapp.core.platform

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.*
import com.google.android.material.snackbar.Snackbar
import com.tawa.allinapp.AndroidApplication
import com.tawa.allinapp.R
import com.tawa.allinapp.core.di.ApplicationComponent
import com.tawa.allinapp.core.dialog.MessageDialogFragment
import com.tawa.allinapp.core.dialog.ProgressDialogFragment
import com.tawa.allinapp.core.extensions.appContext
import com.tawa.allinapp.core.extensions.viewContainer
import com.tawa.allinapp.core.functional.Failure
import com.tawa.allinapp.features.HomeActivity
import com.tawa.allinapp.features.auth.ui.LoginActivity
import kotlinx.android.synthetic.main.toolbar.*
import java.util.*
import javax.inject.Inject

abstract class BaseFragment : Fragment() {

    private var errorDialog: MessageDialogFragment? = null
    private var progressDialog: ProgressDialogFragment? = null


    val appComponent: ApplicationComponent by lazy(mode = LazyThreadSafetyMode.NONE) {
        (activity?.application as AndroidApplication).appComponent
    }

    val current: Date = Calendar.getInstance().time

    var dayWeek:String = ""
    var year:String = ""

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

    open fun onBackPressed() {}

    internal fun firstTimeCreated(savedInstanceState: Bundle?) = savedInstanceState == null

    internal fun showProgress() = progressStatus(View.VISIBLE)

    internal fun hideProgress() = progressStatus(View.GONE)

    private fun progressStatus(viewStatus: Int) = with(activity) {
        if (this is BaseActivity) this.progress.visibility = viewStatus
    }


    internal fun showProgressDialog() {
        progressDialog?.let {
            progressDialog?.show(childFragmentManager, "loading")
        } ?: run {
            progressDialog = ProgressDialogFragment()
            progressDialog?.show(childFragmentManager, "loading")
        }
    }

    internal fun hideProgressDialog() {
        progressDialog?.dismiss()
    }

    internal  fun getDayWeek()= when(current.day){
            0 ->  "Domingo"
            1 -> "Lunes"
            2 -> "Martes"
            3 -> "Miercoles"
            4 ->  "Jueves"
            5 ->  "Viernes"
            6 -> "Sabado"
        else -> ""

    }

    internal  fun getDayMonth() = current.date.toString()

    internal  fun getYear() = "${current.year + 1900}"

    internal  fun getMonth() = when (current.month) {
        0 -> "Enero"
        1 -> "Febrero"
        2 -> "Marzo"
        3 -> "Abril"
        4 -> "Mayo"
        5 -> "Junio"
        6 ->  "Julio"
        7 ->  "Agosto"
        8 ->  "Septiembre"
        9 ->  "Octubre"
        10 -> "Noviembre"
        11 ->  "Diciembre"
        else -> ""
    }

    open fun showMessage(message:String?){
        val dialog = MessageDialogFragment.newInstance(message ?:"")
        dialog.show(childFragmentManager, "dialog")
    }

    open fun handleFailure(failure: Failure?) {
        hideProgressDialog()
        errorDialog = null
        failure?.let {
            errorDialog = when (it) {
                is Failure.DefaultError -> {
                    MessageDialogFragment.newInstance(it.message ?: getString(R.string.error_unknown))
                }
                is Failure.NetworkConnection -> {
                    MessageDialogFragment.newInstance(getString(R.string.error_network))
                }
                else -> {
                    MessageDialogFragment.newInstance(getString(R.string.error_unknown))
                }
            }
            errorDialog?.listener = object : MessageDialogFragment.Callback {
                override fun onAccept() {}
            }
            errorDialog?.show(childFragmentManager, "dialog")
        }
    }

    fun showHome(context: Context?) = context?.let { it.startActivity(Intent(it, HomeActivity::class.java)) }

    fun showLogin(context: Context?) = context?.let { it.startActivity(Intent(it, LoginActivity::class.java)) }

    internal fun notify(@StringRes message: Int) =
        Snackbar.make(viewContainer, message, Snackbar.LENGTH_SHORT).show()

    internal fun notifyWithAction(@StringRes message: Int, @StringRes actionText: Int, action: () -> Any) {
        val snackBar = Snackbar.make(viewContainer, message, Snackbar.LENGTH_INDEFINITE)
        snackBar.setAction(actionText) { _ -> action.invoke() }
        snackBar.setActionTextColor(ContextCompat.getColor(appContext, R.color.colorTextPrimary))
        snackBar.show()
    }




}