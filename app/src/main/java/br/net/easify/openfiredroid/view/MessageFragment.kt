package br.net.easify.openfiredroid.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import br.net.easify.openfiredroid.R
import br.net.easify.openfiredroid.viewmodel.MessageViewModel

class MessageFragment : Fragment() {
    private var contactId = 0L

    private lateinit var viewModel: MessageViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()

        arguments?.let {
            this.contactId = MessageFragmentArgs.fromBundle(it).contactId
        }

        return inflater.inflate(R.layout.fragment_message, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(MessageViewModel::class.java)

        viewModel.loadChat(this.contactId)
    }
}