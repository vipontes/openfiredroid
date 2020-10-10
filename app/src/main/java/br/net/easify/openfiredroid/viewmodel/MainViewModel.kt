package br.net.easify.openfiredroid.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import br.net.easify.openfiredroid.MainApplication
import br.net.easify.openfiredroid.database.AppDatabase
import br.net.easify.openfiredroid.xmpp.XMPP
import javax.inject.Inject

class MainViewModel(application: Application) : AndroidViewModel(application) {
    val userAlreadyLogged by lazy { MutableLiveData<Boolean>() }

    @Inject
    lateinit var database: AppDatabase

    init {
        (getApplication() as MainApplication).getAppComponent()?.inject(this)
    }

    fun checkLoggedUser() {
        val user = database.userDao().getLoggedUser()
        user?.let {
            val userName = it.user_name
            val password = it.password
            if ( userName.isNotEmpty() && password.isNotEmpty()) {
                userAlreadyLogged.value = true
                XMPP.getXmpp(getApplication())?.login(userName, password)
            }
        }
    }

    fun logout() {
        database.contactDao().deleteAll()
        database.userDao().delete()
        database.chatDao().deleteAll()
        XMPP.getXmpp(getApplication())?.close()
    }
}