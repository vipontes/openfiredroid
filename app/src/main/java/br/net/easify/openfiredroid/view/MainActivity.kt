package br.net.easify.openfiredroid.view

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import br.net.easify.openfiredroid.R
import br.net.easify.openfiredroid.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel
    private lateinit var navController: NavController

    private val userAlreadyLoggedObserver = Observer<Boolean> { data: Boolean ->
        data.let {
            if ( it ) {
                val action = LoginFragmentDirections.actionLogin()
                navController.navigate(action)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navController = Navigation.findNavController(this, R.id.mainFragment)

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        viewModel.userAlreadyLogged.observe(this, userAlreadyLoggedObserver)
        viewModel.checkLoggedUser()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.exit -> {
                val action = ContactsFragmentDirections.actionLogout()
                navController.navigate(action)
                viewModel.logout()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}