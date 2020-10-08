package br.net.easify.openfiredroid.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import br.net.easify.openfiredroid.R
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_login.login
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        backToLogin.setOnClickListener {
            val action = ProfileFragmentDirections.actionLogout()
            Navigation.findNavController(it).navigate(action)
        }

        backToContact.setOnClickListener {
            val action = ProfileFragmentDirections.actionLeaveProfile()
            Navigation.findNavController(it).navigate(action)
        }
    }
}