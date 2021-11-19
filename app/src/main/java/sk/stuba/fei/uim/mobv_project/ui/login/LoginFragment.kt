package sk.stuba.fei.uim.mobv_project.ui.login

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import sk.stuba.fei.uim.mobv_project.R
import sk.stuba.fei.uim.mobv_project.databinding.FragmentLoginBinding
import sk.stuba.fei.uim.mobv_project.ui.abstracts.FullscreenFragment

class LoginFragment : FullscreenFragment<FragmentLoginBinding>(
    R.layout.fragment_login,
    FragmentLoginBinding::bind
) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding!!.submitLoginButton.setOnClickListener {
            //TODO: validate against DB
            //TODO: if already has a private key then navigate to My Balance
            findNavController().navigate(
                LoginFragmentDirections.actionLoginFragmentToIntroFragment()
            )
        }
    }
}