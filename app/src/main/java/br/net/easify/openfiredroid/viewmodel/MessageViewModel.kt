package br.net.easify.openfiredroid.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import br.net.easify.openfiredroid.MainApplication
import br.net.easify.openfiredroid.database.AppDatabase
import br.net.easify.openfiredroid.database.model.Chat
import br.net.easify.openfiredroid.database.model.Contact
import javax.inject.Inject

class MessageViewModel(application: Application) : AndroidViewModel(application) {

    val chatMessages by lazy { MutableLiveData<List<Chat>>() }

    @Inject
    lateinit var database: AppDatabase

    init {
        (getApplication() as MainApplication).getAppComponent()?.inject(this)
    }

    fun loadChat(contactId: Long) {
        chatMessages.value =
            database.chatDao().getChatFromContact(contactId)
    }
}