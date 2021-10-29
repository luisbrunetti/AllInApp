package com.tawa.allinapp.core.extensions

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.tawa.allinapp.R

fun ViewGroup.inflate(@LayoutRes layoutRes: Int): View =
    LayoutInflater.from(context).inflate(layoutRes, this, false)

fun ImageView.loadFromUrl(url: String) =
    Glide.with(this.context.applicationContext)
        .load(url)
        .transform(CenterCrop(),RoundedCorners(25))
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(this)

fun ImageView.loadBitmap(url: Bitmap) =
    Glide.with(this.context.applicationContext)
        .load(url)
        .transform(CenterCrop(),RoundedCorners(25))
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(this)

fun ImageView.loadFromResource(resource: Int) =
    Glide.with(this.context.applicationContext)
        .load(resource)
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(this)

fun View.visible() { this.visibility = View.VISIBLE }

fun View.invisible() { this.visibility = View.GONE }

fun Double.round(decimals: Int): Double {
    var multiplier = 1.0
    repeat(decimals) { multiplier *= 10 }
    return round((this * multiplier).toInt()) / multiplier
}

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

@BindingAdapter("visibleView")
fun View.setVisibility(value: Boolean?){
    value?.let { enable ->
        if (enable) visible()
        else invisible()
    }
}

@BindingAdapter("bgError")
fun View.setErrorBg(value: Boolean?){
    value?.let { enable ->
        background =
            if (enable) ResourcesCompat.getDrawable(resources, R.drawable.bg_error_text, null)
            else ResourcesCompat.getDrawable(resources, R.drawable.selector, null)
    }
}

@BindingAdapter("checkModeBtn")
fun Button.setCheckModeBtn(value: Boolean?){
    value?.let { enable ->
        background =
            if (enable) {
                ResourcesCompat.getDrawable(resources, R.drawable.bg_button_check_in, null)
            }
            else{
                ResourcesCompat.getDrawable(resources, R.drawable.bg_button_check_out, null)
            }
        text =
            if (enable) resources.getString(R.string.text_check_in)
            else resources.getString(R.string.text_check_out)
    }
}