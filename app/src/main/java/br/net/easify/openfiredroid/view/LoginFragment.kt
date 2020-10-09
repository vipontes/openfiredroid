package br.net.easify.openfiredroid.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import br.net.easify.openfiredroid.R
import br.net.easify.openfiredroid.databinding.FragmentLoginBinding
import br.net.easify.openfiredroid.model.Login
import br.net.easify.openfiredroid.viewmodel.LoginViewModel


class LoginFragment : Fragment() {

    private lateinit var viewModel: LoginViewModel
    private lateinit var dataBinding: FragmentLoginBinding
    private lateinit var login: Login

    private val loginObserver = Observer<Login> { data: Login ->
        data.let {
            login = it
            dataBinding.login = login
        }
    }

    private val serverOnObserver = Observer<Boolean> { data: Boolean ->
        data.let {
            if ( it ) {
                val action = LoginFragmentDirections.actionLogin()
                Navigation.findNavController(dataBinding.loginButton).navigate(action)
            } else {
                Toast.makeText(requireContext(),
                    getString(R.string.login_error),
                    Toast.LENGTH_LONG).show()
            }
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
        viewModel.login.observe(viewLifecycleOwner, loginObserver)
        viewModel.serverOn.observe(viewLifecycleOwner, serverOnObserver)

        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataBinding.loginButton.setOnClickListener {
            viewModel.login()
        }
    }
}