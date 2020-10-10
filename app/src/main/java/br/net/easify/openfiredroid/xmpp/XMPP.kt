package br.net.easify.openfiredroid.xmpp

import android.content.Context
import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import br.net.easify.openfiredroid.database.model.Contact
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jivesoftware.smack.ConnectionConfiguration
import org.jivesoftware.smack.SmackException
import org.jivesoftware.smack.chat2.Chat
import org.jivesoftware.smack.chat2.ChatManager
import org.jivesoftware.smack.roster.Roster
import org.jivesoftware.smack.roster.RosterEntry
import org.jivesoftware.smack.tcp.XMPPTCPConnection
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration
import org.jxmpp.jid.impl.JidCreate
import java.net.InetAddress
import javax.net.ssl.HostnameVerifier


class XMPP(private var context: Context) {
    private val port = 5222
    private val host = "192.168.0.17"

    companion object {
        private var instance: XMPP? = null
        fun getXmpp(context: Context): XMPP? {
            if (instance == null) {
                synchronized(XMPP::class) {
                    instance = XMPP(context)
                }
            }
            return instance
        }

        const val serverOn = "XMPP Server On"
        const val loginError = "XMPP Server Login Error"
    }

    private lateinit var connection: XMPPTCPConnection

    private fun buildConfiguration(): XMPPTCPConnectionConfiguration {
        val addr: InetAddress = InetAddress.getByName(host)
        val verifier =
            HostnameVerifier { hostname, session -> false }

        val serviceName = JidCreate.domainBareFrom(host)

        return XMPPTCPConnectionConfiguration.builder()
            .setPort(port)
            .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
            .setXmppDomain(serviceName)
            .setHostnameVerifier(verifier)
            .setHostAddress(addr)
            .setDebuggerEnabled(true)
            .build()
    }

    fun close() {
        if (this::connection.isInitialized) {
            connection.disconnect()
        }
    }

    private fun isConnected(): Boolean {
        return this::connection.isInitialized &&
                connection.isConnected
    }

    private fun isLoggedIn(): Boolean {
        return this::connection.isInitialized &&
                connection.isConnected &&
                connection.isAuthenticated
    }

    private fun getConnection(): XMPPTCPConnection {
        if (isConnected()) {
            return this.connection
        }

        if (this::connection.isInitialized) {
            connection.connect()
        } else {
            val config = buildConfiguration()
            connection = XMPPTCPConnection(config)
            connection.connect()
        }

        return this.connection
    }

    fun login(user: String, password: String) {
        GlobalScope.launch {
            val broadcastManager =
                LocalBroadcastManager.getInstance(context)
            try {
                val connect = getConnection()
                if (!connect.isAuthenticated) {
                    connection.login(user, password)
                }

                broadcastManager.sendBroadcast(Intent(serverOn))
            } catch (error: SmackException) {
                error.printStackTrace()
                broadcastManager.sendBroadcast(Intent(loginError))
            } catch (error: IllegalArgumentException) {
                error.printStackTrace()
                broadcastManager.sendBroadcast(Intent(loginError))
            }
        }
    }

    fun rosters(): List<Contact> {
        val contacts: ArrayList<Contact> = arrayListOf()

        if (isLoggedIn()) {
            val roster = Roster.getInstanceFor(connection)
            val entries: Collection<RosterEntry> = roster.entries
            for (entry in entries) {
                val name = entry.jid.localpartOrNull.toString()
                val jid = entry.jid.toString()
                if (name.isNotEmpty() && jid.isNotEmpty()) {
                    contacts.add(Contact(0, name, jid))
                }
            }
        }

        return contacts
    }

    fun sendMessage(userJid: String, message: String) {
        if (this::connection.isInitialized &&
            connection.isConnected &&
            connection.isAuthenticated
        ) {
            GlobalScope.launch {
                val chatManager: ChatManager = ChatManager.getInstanceFor(connection)
                val jid = JidCreate.entityBareFrom(userJid)
                val chat: Chat = chatManager.chatWith(jid)
                chat.send(message)
            }
        }
    }
}

//chatManager.addIncomingListener { from, message, chat -> println("New message from " + from + ": " + message.body) }
