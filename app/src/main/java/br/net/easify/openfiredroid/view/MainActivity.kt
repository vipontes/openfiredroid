package br.net.easify.openfiredroid.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.view.ActionMode
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import br.net.easify.openfiredroid.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}