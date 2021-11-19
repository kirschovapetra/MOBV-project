package sk.stuba.fei.uim.mobv_project.ui.login

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import sk.stuba.fei.uim.mobv_project.R
import sk.stuba.fei.uim.mobv_project.databinding.FragmentCreatePinBinding
import sk.stuba.fei.uim.mobv_project.ui.abstracts.FullscreenFragment

class CreatePinFragment : FullscreenFragment<FragmentCreatePinBinding>(
    R.layout.fragment_create_pin,
    FragmentCreatePinBinding::bind
) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding!!.submitRegisterButton.setOnClickListener {
            //TODO: validate whether pin and confirmation pin match
            //TODO: save into DB
            findNavController().navigate(
                CreatePinFragmentDirections.actionCreatePinFragmentToLoginFragment()
            )
        }
    }
}