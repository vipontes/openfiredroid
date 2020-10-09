package br.net.easify.openfiredroid.xmpp

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jivesoftware.smack.ConnectionConfiguration
import org.jivesoftware.smack.SmackException
import org.jivesoftware.smack.chat2.Chat
import org.jivesoftware.smack.chat2.ChatManager
import org.jivesoftware.smack.tcp.XMPPTCPConnection
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration
import org.jxmpp.jid.impl.JidCreate
import java.net.InetAddress
import javax.net.ssl.HostnameVerifier


class XMPP {
    val PORT = 5222
    val HOST = "192.168.0.17"

    private lateinit var connection: XMPPTCPConnection

    private fun buildConfiguration(): XMPPTCPConnectionConfiguration {
        val addr: InetAddress = InetAddress.getByName(HOST)
        val verifier =
            HostnameVerifier { hostname, session -> false }

        val serviceName = JidCreate.domainBareFrom(HOST)

        return XMPPTCPConnectionConfiguration.builder()
            .setPort(PORT)
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

    fun isConnected(): Boolean {
        return this::connection.isInitialized && connection.isConnected
    }

    fun isLoggedIn(): Boolean {
        return this::connection.isInitialized && connection.isConnected && connection.isAuthenticated
    }

    private fun getConnection(): XMPPTCPConnection {
        if (isConnected()) {
            return this.connection;
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
            try {
                val connect = getConnection()
                if (!connect.isAuthenticated) {
                    connection.login(user, password)
                }
            } catch (error: SmackException) {
                error.printStackTrace()
            }
        }
    }

    fun sendMessage() {

        if (this::connection.isInitialized &&
            connection.isConnected &&
            connection.isAuthenticated) {
            GlobalScope.launch {
                val chatManager: ChatManager = ChatManager.getInstanceFor(connection)
                chatManager.addIncomingListener { from, message, chat -> println("New message from " + from + ": " + message.body) }
                val jid = JidCreate.entityBareFrom("vinicius@easify")
                val chat: Chat = chatManager.chatWith(jid)
                chat.send("Howdy!")
            }
        }
    }

}