package br.net.easify.openfiredroid.service

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import br.net.easify.openfiredroid.MainApplication
import br.net.easify.openfiredroid.R
import br.net.easify.openfiredroid.database.AppDatabase
import br.net.easify.openfiredroid.util.Constants
import br.net.easify.openfiredroid.view.MainActivity
import br.net.easify.openfiredroid.xmpp.XMPP
import org.jivesoftware.smack.SmackException
import org.jivesoftware.smack.XMPPException
import org.jivesoftware.smack.chat2.Chat
import org.jivesoftware.smack.chat2.ChatManager
import org.jivesoftware.smack.chat2.IncomingChatMessageListener
import org.jivesoftware.smack.packet.Message
import org.jxmpp.jid.EntityBareJid
import java.io.IOException
import javax.inject.Inject


class MessageService : Service(), IncomingChatMessageListener {
    private lateinit var connection: XMPP
    private lateinit var chatManager: ChatManager

    @Inject
    lateinit var database: AppDatabase

    override fun onCreate() {
        super.onCreate()
        (application as MainApplication).getAppComponent()?.inject(this)
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    private fun initConnection() {
        if (!this::connection.isInitialized || !connection.isConnected()) {
            connection = XMPP.getXmpp(application)!!
        }

        chatManager = ChatManager.getInstanceFor(connection.getConnection())
        chatManager.addIncomingListener(this)

        try {
            if ( !connection.isLoggedIn() ) {
                val loggedUser = database.userDao().getLoggedUser()
                loggedUser?.let {
                    connection.let { conn ->
                        conn.login(it.user_name, it.password)
                    }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
            stopSelf()
        } catch (e: SmackException) {
            e.printStackTrace()
            stopSelf()
        } catch (e: XMPPException) {
            e.printStackTrace()
            stopSelf()
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        initConnection()
        startForegroundService()
        return START_STICKY
    }

    override fun onDestroy() {
        stopForegroundService()
        super.onDestroy()
    }

    private fun startForegroundService() {
        val notificationIntent =
            Intent(applicationContext, MainActivity::class.java)
        val contentIntent = PendingIntent.getActivity(
            this,
            0,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT)
        val notification =
            NotificationCompat.Builder(this, Constants.foregroundServiceChannelId)
                .setContentTitle("OpenfireDroid")
                .setContentText("Message Service")
                .setContentIntent(contentIntent)
                .setSmallIcon(R.drawable.ic_android)
                .build()

        startForeground(999, notification)
    }

    private fun stopForegroundService() {
        stopForeground(true)
    }

    override fun newIncomingMessage(from: EntityBareJid?, message: Message?, chat: Chat?) {
        from?.let {
            message?.let { msg ->
                val messageContact = from.localpart.toString()
                val messageBody = msg.body
                val broadcastManager =
                    LocalBroadcastManager.getInstance(application)
                val intent = Intent(XMPP.newMessage)
                intent.putExtra("messageContact", messageContact)
                intent.putExtra("messageBody", messageBody)
                broadcastManager.sendBroadcast(intent)
            }
        }
    }
}