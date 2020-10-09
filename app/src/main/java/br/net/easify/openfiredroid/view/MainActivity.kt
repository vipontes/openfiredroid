package br.net.easify.openfiredroid.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import br.net.easify.openfiredroid.R
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jivesoftware.smack.AbstractXMPPConnection
import org.jivesoftware.smack.ConnectionConfiguration
import org.jivesoftware.smack.SmackException
import org.jivesoftware.smack.chat2.Chat
import org.jivesoftware.smack.chat2.ChatManager
import org.jivesoftware.smack.tcp.XMPPTCPConnection
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration
import org.jxmpp.jid.impl.JidCreate
import java.net.InetAddress
import javax.net.ssl.HostnameVerifier


class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var  connection: AbstractXMPPConnection

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navController = Navigation.findNavController(this, R.id.mainFragment)
//        val action = LoginFragmentDirections.actionLogin()
//        navController.navigate(action)

        GlobalScope.launch {
            setConnection()
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        connection?.let {
            it.disconnect()
        }
    }


    fun setConnection() {
        val addr: InetAddress = InetAddress.getByName("192.168.0.17")
        val verifier =
            HostnameVerifier { hostname, session -> false }

        val serviceName = JidCreate.domainBareFrom("192.168.0.17")

        val config: XMPPTCPConnectionConfiguration = XMPPTCPConnectionConfiguration.builder()
            .setUsernameAndPassword("user1", "pass1234")
            .setPort(5222)
            .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
            .setXmppDomain(serviceName)
            .setHostnameVerifier(verifier)
            .setHostAddress(addr)
            .setDebuggerEnabled(true)
            .build()

        try {
            connection = XMPPTCPConnection(config)
            connection.connect()
            connection.login()

            if (connection.isConnected && connection.isAuthenticated) {
                val chatManager: ChatManager = ChatManager.getInstanceFor(connection)
                chatManager.addIncomingListener { from, message, chat -> println("New message from " + from + ": " + message.body) }
                val jid = JidCreate.entityBareFrom("vinicius@easify")
                val chat: Chat = chatManager.chatWith(jid)
                chat.send("Howdy!")
            }
        } catch (error: SmackException) {
            error.printStackTrace()
        }

    }
}