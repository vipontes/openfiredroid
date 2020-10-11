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
import br.net.easify.openfiredroid.database.model.Chat
import br.net.easify.openfiredroid.database.model.Contact
import br.net.easify.openfiredroid.util.Formatter
import br.net.easify.openfiredroid.util.NotificationHelper
import br.net.easify.openfiredroid.xmpp.XMPP
import javax.inject.Inject

class MessageViewModel(application: Application) : AndroidViewModel(application) {

    val chatMessages by lazy { MutableLiveData<List<Chat>>() }
    val message by lazy { MutableLiveData<Chat>() }
    private var contact: Contact? = null

    @Inject
    lateinit var database: AppDatabase

    private val onNewMessage: BroadcastReceiver =
        object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                loadChat(contact!!.contact_id)
            }
        }

    init {
        (getApplication() as MainApplication).getAppComponent()?.inject(this)

        message.value = Chat(0, 0, "", "", true)

        val newMessageIntent = IntentFilter(XMPP.newMessage)
        LocalBroadcastManager.getInstance(getApplication())
            .registerReceiver(onNewMessage, newMessageIntent)
    }

    fun loadChat(contactId: Long) {
        chatMessages.value =
            database.chatDao().getChatFromContact(contactId)
    }

    fun loadContactInfo(contactId: Long) {
        contact = database.contactDao().get(contactId)
    }

    fun sendMessage() {
        message.value?.let {
            if (it.message.isNotEmpty()) {
                XMPP.getXmpp(getApplication())?.sendMessage(
                    contact!!.contact_jid,
                    it.message
                )

                it.contact_id = contact!!.contact_id
                it.date = Formatter.currentDateTimeYMDAsString()
                it.owner = true

                database.chatDao().insert(it)

                loadChat(contact!!.contact_id)

                message.value = Chat(0, 0, "", "", true)
            }
        }
    }

    fun deleteMessage(chatId: Long) {
        database.chatDao().delete(chatId)
        loadChat(contact!!.contact_id)
    }
}