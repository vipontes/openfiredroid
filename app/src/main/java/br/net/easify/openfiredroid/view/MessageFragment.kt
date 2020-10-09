package br.net.easify.openfiredroid.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import br.net.easify.openfiredroid.R
import br.net.easify.openfiredroid.viewmodel.MessageViewModel

class MessageFragment : Fragment() {
    private lateinit var viewModel: MessageViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_message, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(MessageViewModel::class.java)


//        profile.setOnClickListener {
//            val action = MessageFragmentDirections.actionLeaveMessage()
//            Navigation.findNavController(it).navigate(action)
//        }
    }
}