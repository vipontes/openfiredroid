package br.net.easify.openfiredroid.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import br.net.easify.openfiredroid.R
import br.net.easify.openfiredroid.databinding.FragmentLoginBinding
import br.net.easify.openfiredroid.viewmodel.LoginViewModel
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : Fragment() {

    private lateinit var viewModel: LoginViewModel
    private lateinit var dataBinding: FragmentLoginBinding

    private val userObserver = Observer<String> { data: String ->
        data.let {
            dataBinding.user = it
        }
    }

    private val passwordObserver = Observer<String> { data: String ->
        data.let {
            dataBinding.password = it
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_login,
            container,
            false
        )

        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        viewModel.user.observe(viewLifecycleOwner, userObserver)
        viewModel.password.observe(viewLifecycleOwner, passwordObserver)

        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        login.setOnClickListener {
//            val action = LoginFragmentDirections.actionLogin()
//            Navigation.findNavController(it).navigate(action)

            viewModel.login()
        }
    }

}