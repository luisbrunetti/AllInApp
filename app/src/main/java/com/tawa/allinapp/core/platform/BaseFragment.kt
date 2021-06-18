package com.tawa.allinapp.core.platform

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.tawa.allinapp.R
import com.google.android.material.snackbar.Snackbar
import com.tawa.allinapp.AndroidApplication
import com.tawa.allinapp.core.di.ApplicationComponent
import com.tawa.allinapp.core.dialog.MessageDialogFragment
import com.tawa.allinapp.core.dialog.ProgressDialogFragment
import com.tawa.allinapp.core.extensions.appContext
import com.tawa.allinapp.core.extensions.viewContainer
import com.tawa.allinapp.core.functional.Failure
import com.tawa.allinapp.features.HomeActivity
import com.tawa.allinapp.features.auth.ui.LoginActivity
import kotlinx.android.synthetic.main.toolbar.*
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.Month
import javax.inject.Inject

abstract class BaseFragment : Fragment() {

    private var errorDialog: MessageDialogFragment? = null
    private var progressDialog: ProgressDialogFragment? = null

    val appComponent: ApplicationComponent by lazy(mode = LazyThreadSafetyMode.NONE) {
        (activity?.application as AndroidApplication).appComponent
    }

    val current: LocalDateTime = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        LocalDateTime.now()

    } else {
        TODO("VERSION.SDK_INT < O")
    }

    var dayWeek:String = ""
    var dayMonth:String = ""
    var month:String = ""
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

    @RequiresApi(Build.VERSION_CODES.O)
    internal  fun getDayWeek():String{
        when(current.dayOfWeek){

            DayOfWeek.SUNDAY -> dayWeek= "Domingo"
            DayOfWeek.MONDAY -> dayWeek= "Lunes"
            DayOfWeek.TUESDAY -> dayWeek= "Martes"
            DayOfWeek.WEDNESDAY -> dayWeek= "Miercoles"
            DayOfWeek.THURSDAY -> dayWeek= "Jueves"
            DayOfWeek.FRIDAY -> dayWeek= "Viernes"
            DayOfWeek.SATURDAY -> dayWeek= "Sabado"

        }

        return dayWeek
    }

    @RequiresApi(Build.VERSION_CODES.O)
    internal  fun getDayMonth():String {

        dayMonth = current.dayOfMonth.toString()

        return dayMonth
    }

    @RequiresApi(Build.VERSION_CODES.O)
    internal  fun getYear():String {

        year = current.year.toString()

        return year
    }


    @RequiresApi(Build.VERSION_CODES.O)
    internal  fun getMonth():String{

        when (current.month)
        {
            Month.JANUARY -> month = "Enero"
            Month.FEBRUARY -> month = "Febrero"
            Month.MARCH -> month= "Marzo"
            Month.APRIL -> month = "Abril"
            Month.MAY -> month= "Mayo"
            Month.JUNE -> month= "Junio"
            Month.JULY -> month= "Julio"
            Month.AUGUST -> month = "Agosto"
            Month.SEPTEMBER -> month = "Septiembre"
            Month.OCTOBER -> month= "Octubre"
            Month.NOVEMBER -> month = "Noviembre"
            Month.DECEMBER -> month= "Diciembre"
        }

        return month
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