package com.tawa.allinapp.core.extensions

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.tawa.allinapp.R

fun ViewGroup.inflate(@LayoutRes layoutRes: Int): View =
    LayoutInflater.from(context).inflate(layoutRes, this, false)

fun ImageView.loadFromUrl(url: String) =
    Glide.with(this.context.applicationContext)
        .load(url)
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(this)

fun View.visible() { this.visibility = View.VISIBLE }

fun View.invisible() { this.visibility = View.GONE }

@BindingAdapter("enableButton")
fun View.setButtonEnabled(value: Boolean?){
    value?.let { enable ->
        if (enable){
            isEnabled = true
            alpha = 1.0F
        }else{
            isEnabled = false
            alpha = 0.5F
        }
    }
}

@BindingAdapter("bgError")
fun View.setErrorBg(value: Boolean?){
    value?.let { enable ->
        background = if (enable){
            ResourcesCompat.getDrawable(resources, R.drawable.bg_error_text, null)
        }else{
            ResourcesCompat.getDrawable(resources, R.drawable.selector, null)
        }
    }
}