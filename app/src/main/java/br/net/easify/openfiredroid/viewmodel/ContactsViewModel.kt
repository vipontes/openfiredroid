package br.net.easify.openfiredroid.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import br.net.easify.openfiredroid.MainApplication
import br.net.easify.openfiredroid.database.AppDatabase
import br.net.easify.openfiredroid.database.model.Contact
import br.net.easify.openfiredroid.xmpp.XMPP
import javax.inject.Inject

class ContactsViewModel(application: Application) : AndroidViewModel(application) {

    val contacts by lazy { MutableLiveData<List<Contact>>() }

    @Inject
    lateinit var database: AppDatabase

    init {
        (getApplication() as MainApplication).getAppComponent()?.inject(this)
        loadContacts()
    }

    fun loadContacts() {
        val data = database.contactDao().getAll()
        if (data.isEmpty()) {
            loadContactsFromServer()
        } else {
            contacts.value = data
        }
    }

    private fun loadContactsFromServer() {
        val xmpp = XMPP.getXmpp(getApplication())
        xmpp?.let {
            val serverContacts = xmpp.rosters()
            database.contactDao().deleteAll()
            database.contactDao().insert(serverContacts)
            val data = database.contactDao().getAll()
            contacts.value = data
        }
    }
}