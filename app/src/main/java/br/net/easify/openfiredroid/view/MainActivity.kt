package br.net.easify.openfiredroid.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import br.net.easify.openfiredroid.R
import br.net.easify.openfiredroid.xmpp.XMPP
import org.jivesoftware.smack.AbstractXMPPConnection


class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var  connection: AbstractXMPPConnection

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navController = Navigation.findNavController(this, R.id.mainFragment)
//        val action = LoginFragmentDirections.actionLogin()
//        navController.navigate(action)

        val xmpp = XMPP()
        xmpp.login("user1", "pass1234")
    }
}