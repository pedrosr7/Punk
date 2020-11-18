package com.one2one.test.punk.core.extensions

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.util.TypedValue
import android.view.animation.AnimationUtils
import android.view.animation.Interpolator
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.StyleRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.res.use


val Context.connectivityManager: ConnectivityManager
    get() = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager


val Context.network: Network?
    get() = connectivityManager.activeNetwork

val Context.capabilities: NetworkCapabilities?
    get() = connectivityManager.getNetworkCapabilities(network)

fun Context.isNetworkAvailable(): Boolean {
    if(capabilities == null ) return false
    return when {
        capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)!! -> true
        capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)!! -> true
        capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)!! -> true
        else -> false
    }
}

fun Context.isNotNetworkAvailable(): Boolean {
    if(capabilities == null ) return true
    return ! when {
        capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)!! -> true
        capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)!! -> true
        capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)!! -> true
        else -> false
    }
}

/**
 * Retrieve a color from the current [android.content.res.Resources.Theme].
 */
@ColorInt
@SuppressLint("Recycle")
fun Context.themeColor(
    @AttrRes themeAttrId: Int
): Int {
    return obtainStyledAttributes(
        intArrayOf(themeAttrId)
    ).use {
        it.getColor(0, Color.MAGENTA)
    }
}

/**
 * Retrieve a style from the current [android.content.res.Resources.Theme].
 */
@StyleRes
fun Context.themeStyle(@AttrRes attr: Int): Int {
    val tv = TypedValue()
    theme.resolveAttribute(attr, tv, true)
    return tv.data
}

@SuppressLint("Recycle")
fun Context.themeInterpolator(@AttrRes attr: Int): Interpolator {
    return AnimationUtils.loadInterpolator(
        this,
        obtainStyledAttributes(intArrayOf(attr)).use {
            it.getResourceId(0, android.R.interpolator.fast_out_slow_in)
        }
    )
}

fun Context.getDrawableOrNull(@DrawableRes id: Int?): Drawable? {
    return if (id == null || id == 0) null else AppCompatResources.getDrawable(this, id)
}