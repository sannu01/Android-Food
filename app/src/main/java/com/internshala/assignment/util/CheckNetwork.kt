package com.internshala.assignment.util

import android.content.Context
import android.net.*
import android.net.ConnectivityManager.NetworkCallback
import android.os.Build
import androidx.annotation.RequiresApi


class CheckNetwork {




    @RequiresApi(Build.VERSION_CODES.N)
    fun registerDefaultNetworkCallback(context:Context) {
        try {
            val connectivityManager =
                (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)
            connectivityManager.registerDefaultNetworkCallback(object : NetworkCallback() {
                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    GlobalVars.isNetworkConnected = true

                }

                override fun onLost(network: Network) {
                    super.onLost(network)
                    GlobalVars.isNetworkConnected = false

                }

                override fun onBlockedStatusChanged(
                    network: Network,
                    blocked: Boolean
                ) {
                    super.onBlockedStatusChanged(network, blocked)

                }

                override fun onCapabilitiesChanged(
                    network: Network,
                    networkCapabilities: NetworkCapabilities
                ) {
                    super.onCapabilitiesChanged(network, networkCapabilities)

                }

                override fun onLinkPropertiesChanged(
                    network: Network,
                    linkProperties: LinkProperties
                ) {
                    super.onLinkPropertiesChanged(network, linkProperties)

                }

                override fun onLosing(network: Network, maxMsToLive: Int) {
                    super.onLosing(network, maxMsToLive)

                }

                override fun onUnavailable() {
                    super.onUnavailable()

                }
            })
        } catch (e: Exception) {

            GlobalVars.isNetworkConnected = false
        }
    }





}
object GlobalVars {
    var counter = 0
    var isNetworkConnected = false
}