package br.net.easify.openfiredroid.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import br.net.easify.openfiredroid.R
import kotlinx.android.synthetic.main.fragment_message.*

class MessageFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_message, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profile.setOnClickListener {
            val action = MessageFragmentDirections.actionLeaveMessage()
            Navigation.findNavController(it).navigate(action)
        }
    }
}