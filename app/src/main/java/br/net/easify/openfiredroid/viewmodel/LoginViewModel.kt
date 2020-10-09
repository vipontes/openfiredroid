package br.net.easify.openfiredroid.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import br.net.easify.openfiredroid.MainApplication
import br.net.easify.openfiredroid.database.AppDatabase
import javax.inject.Inject

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    val user by lazy { MutableLiveData<String>() }
    val password by lazy { MutableLiveData<String>() }

    @Inject
    lateinit var database: AppDatabase

    init {
        (getApplication() as MainApplication).getAppComponent()?.inject(this)
        user.value = ""
        password.value = ""
    }

    fun login() {

    }
}