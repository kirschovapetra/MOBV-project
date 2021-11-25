package sk.stuba.fei.uim.mobv_project.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import sk.stuba.fei.uim.mobv_project.R
import sk.stuba.fei.uim.mobv_project.data.view_models.login.LoginViewModel
import sk.stuba.fei.uim.mobv_project.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private val viewModel: LoginViewModel by viewModels()
    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_login,
            container,
            false
        )
        binding.loginViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.eventPinCorrect.observe(viewLifecycleOwner, { isCorrect ->
            isCorrect.getContentIfNotHandled().let {
                if (true == it) {
                    onPinCorrect()
                }
                else {
                    onPinIncorrect()
                }
            }
        })

        return binding.root
    }

    private fun onPinCorrect() {
        //TODO: if already has a private key then navigate to My Balance
//        findNavController().navigate(
//            LoginFragmentDirections.actionLoginFragmentToIntroFragment()
//        LoginFragmentDirections.actionLoginFragmentToContactsFragment()
//        )
    }

    private fun onPinIncorrect() {
        view?.let {
            //TODO: replace hardcoded text
            Snackbar.make(it, "Invalid pin", Snackbar.LENGTH_SHORT)
                    .show()
        }
    }
}