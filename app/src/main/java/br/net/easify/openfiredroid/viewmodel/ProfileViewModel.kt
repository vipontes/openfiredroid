package br.net.easify.openfiredroid.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import br.net.easify.openfiredroid.MainApplication
import br.net.easify.openfiredroid.database.AppDatabase
import javax.inject.Inject

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    @Inject
    lateinit var database: AppDatabase

    init {
        (getApplication() as MainApplication).getAppComponent()?.inject(this)
    }
}