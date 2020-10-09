package br.net.easify.openfiredroid.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import br.net.easify.openfiredroid.R
import br.net.easify.openfiredroid.viewmodel.ContactsViewModel

class ContactsFragment : Fragment() {

    private lateinit var viewModel: ContactsViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contacts, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(ContactsViewModel::class.java)


//        messages.setOnClickListener {
//            val action = ContactsFragmentDirections.actionViewMessage()
//            Navigation.findNavController(it).navigate(action)
//        }
//
//        profile.setOnClickListener {
//            val action = ContactsFragmentDirections.actionViewProfile()
//            Navigation.findNavController(it).navigate(action)
//        }
    }


}