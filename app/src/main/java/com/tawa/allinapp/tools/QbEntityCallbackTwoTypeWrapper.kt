package com.tawa.allinapp.tools
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.quickblox.core.QBEntityCallback
import com.quickblox.core.exception.QBResponseException
import com.tawa.allinapp.features.messages.ui.MessagesFragment

open class QbEntityCallbackTwoTypeWrapper<T, R>(private var callback: QBEntityCallback<R>) : QBEntityCallback<T> {
    private val mainThreadHandler = Handler(Looper.getMainLooper())

    override fun onSuccess(t: T, bundle: Bundle?) {
        Log.d("onSucess","Sucess")
    }

    override fun onError(error: QBResponseException) {
        error.message?.let { Log.d(MessagesFragment.TAG, error.toString()) }
        onErrorInMainThread(error)
    }

    protected fun onSuccessInMainThread(result: R, bundle: Bundle?) {
        mainThreadHandler.post { callback.onSuccess(result, bundle) }
    }

    private fun onErrorInMainThread(error: QBResponseException) {
        mainThreadHandler.post { callback.onError(error) }
    }
}