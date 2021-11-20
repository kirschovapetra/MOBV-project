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
import sk.stuba.fei.uim.mobv_project.data.view_models.login.CreatePinViewModel
import sk.stuba.fei.uim.mobv_project.databinding.FragmentCreatePinBinding

class CreatePinFragment : Fragment() {

    private val viewModel: CreatePinViewModel by viewModels()
    private lateinit var binding: FragmentCreatePinBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_create_pin,
            container,
            false
        )
        binding.createPinViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.eventPinMatch.observe(viewLifecycleOwner, { pinMatch ->
            pinMatch.getContentIfNotHandled().let {
                if (false == it) {
                    onPinMismatch()
                }
            }
        })
        viewModel.eventRegistered.observe(viewLifecycleOwner, { registered ->
            registered.getContentIfNotHandled().let {
                if (true == it) {
                    onRegistered()
                }
            }
        })

        return binding.root
    }

    private fun onPinMismatch() {
        view?.let {
            //TODO: remove hardcoded text
            Snackbar.make(it, "Pins do not match", Snackbar.LENGTH_SHORT)
                .show()
        }
    }

    private fun onRegistered() {
        findNavController().navigate(
            CreatePinFragmentDirections.actionCreatePinFragmentToLoginFragment()
        )
    }
}