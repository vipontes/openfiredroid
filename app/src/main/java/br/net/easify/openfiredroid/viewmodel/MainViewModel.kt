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
import br.net.easify.openfiredroid.service.MessageService
import br.net.easify.openfiredroid.util.Formatter
import br.net.easify.openfiredroid.util.NotificationHelper
import br.net.easify.openfiredroid.util.ServiceHelper
import br.net.easify.openfiredroid.xmpp.XMPP
import javax.inject.Inject

class MainViewModel(application: Application) : AndroidViewModel(application) {
    val userAlreadyLogged by lazy { MutableLiveData<Boolean>() }

    @Inject
    lateinit var database: AppDatabase

    @Inject
    lateinit var serviceHelper: ServiceHelper

    private val onNewMessage: BroadcastReceiver =
        object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {

                val messageBody = intent.getStringExtra("messageBody")
                val messageContact = intent.getStringExtra("messageContact")
                messageContact?.let { user ->
                    val contact = database.contactDao().getContactFromName(user)
                    contact?.let {
                        messageBody?.let {body ->
                            val messageDate = Formatter.currentDateTimeYMDAsString()
                            database.chatDao().insert(
                                Chat(0, contact.contact_id, body, messageDate, false)
                            )

                            NotificationHelper(application).createNotification(user, body)
                        }
                    }
                }

            }
        }

    init {
        (getApplication() as MainApplication).getAppComponent()?.inject(this)

        val newMessageIntent = IntentFilter(XMPP.newMessage)
        LocalBroadcastManager.getInstance(getApplication())
            .registerReceiver(onNewMessage, newMessageIntent)
    }

    fun checkLoggedUser() {
        val user = database.userDao().getLoggedUser()
        user?.let {
            val userName = it.user_name
            val password = it.password
            if (userName.isNotEmpty() && password.isNotEmpty()) {
                userAlreadyLogged.value = true
                XMPP.getXmpp(getApplication())?.login(userName, password)
                startMessageService()
            }
        }
    }

    fun logout() {
        database.userDao().delete()
//        database.chatDao().deleteAll()
//        database.contactDao().deleteAll()
        XMPP.getXmpp(getApplication())?.close()
    }

    private fun startMessageService() {
        val messageService = MessageService()
        val intent = Intent(getApplication(), messageService::class.java)
        if (!serviceHelper.isMyServiceRunning(messageService::class.java)) {
            (getApplication() as MainApplication).startService(intent)
        }
    }
}