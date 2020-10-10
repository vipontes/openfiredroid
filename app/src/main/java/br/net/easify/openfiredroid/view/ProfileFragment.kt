package br.net.easify.openfiredroid.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import br.net.easify.openfiredroid.R
import br.net.easify.openfiredroid.viewmodel.ProfileViewModel

class ProfileFragment : Fragment() {

    private lateinit var viewModel: ProfileViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(ProfileViewModel::class.java)


//        backToLogin.setOnClickListener {
//            val action = ProfileFragmentDirections.actionLogout()
//            Navigation.findNavController(it).navigate(action)
//        }
//
//        backToContact.setOnClickListener {
//            val action = ProfileFragmentDirections.actionLeaveProfile()
//            Navigation.findNavController(it).navigate(action)
//        }
    }
}