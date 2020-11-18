package com.one2one.test.punk.core.managers

import android.content.Context
import android.net.NetworkCapabilities
import com.one2one.test.punk.core.extensions.connectivityManager


class NetworkManager(private val context: Context) {
    val isNetworkAvailable: Boolean
        get() {
            val capabilities =
                context.connectivityManager.getNetworkCapabilities(context.connectivityManager.activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        }
    val isNotNetworkAvailable: Boolean
        get() {
            val capabilities =
                context.connectivityManager.getNetworkCapabilities(context.connectivityManager.activeNetwork) ?: return true
            return ! when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        }
}
