package br.net.easify.openfiredroid.util

import android.app.ActivityManager
import android.content.Context
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ServiceHelper @Inject constructor(context: Context) {

    private val manager = context.applicationContext.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

    fun isMyServiceRunning(serviceClass: Class<*>): Boolean {
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }
}