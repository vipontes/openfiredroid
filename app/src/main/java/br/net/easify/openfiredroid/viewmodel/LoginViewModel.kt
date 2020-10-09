package br.net.easify.openfiredroid.viewmodel

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import br.net.easify.openfiredroid.MainApplication
import br.net.easify.openfiredroid.database.AppDatabase
import br.net.easify.openfiredroid.database.model.User
import br.net.easify.openfiredroid.model.Login
import br.net.easify.openfiredroid.xmpp.XMPP
import javax.inject.Inject

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    val login by lazy { MutableLiveData<Login>() }
    val serverOn by lazy { MutableLiveData<Boolean>() }

    @Inject
    lateinit var database: AppDatabase

    private val onServerOn: BroadcastReceiver =
        object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                serverOn.value = true
                saveLoginCredentials()
            }
        }

    private val onLoginError: BroadcastReceiver =
        object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                serverOn.value = false
            }
        }

    init {
        (getApplication() as MainApplication).getAppComponent()?.inject(this)
        login.value = Login("", "")

        val serverOnIntent = IntentFilter(XMPP.serverOn)
        LocalBroadcastManager.getInstance(getApplication())
            .registerReceiver(onServerOn, serverOnIntent)

        val loginErrorIntent = IntentFilter(XMPP.loginError)
        LocalBroadcastManager.getInstance(getApplication())
            .registerReceiver(onLoginError, loginErrorIntent)
    }

    private fun saveLoginCredentials() {
        login.value?.let {
            val userName = it.userName
            val password = it.password
            database.userDao().delete()
            database.userDao().insert(User(0, userName, password))
        }
    }

    fun login() {
        login.value?.let {
            val userName = it.userName
            val password = it.password
            XMPP.getXmpp(getApplication())?.login(userName, password)
        }
    }
}