package sk.stuba.fei.uim.mobv_project.ui.intro

import androidx.fragment.app.Fragment
import sk.stuba.fei.uim.mobv_project.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import sk.stuba.fei.uim.mobv_project.data.repositories.AccountRepository
import sk.stuba.fei.uim.mobv_project.data.utils.ViewModelFactory
import sk.stuba.fei.uim.mobv_project.data.view_models.intro.IntroViewModel
import sk.stuba.fei.uim.mobv_project.databinding.FragmentIntroBinding

class IntroFragment : Fragment() {

    private lateinit var binding: FragmentIntroBinding
    private val viewModel: IntroViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_intro,
            container,
            false
        )
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        viewModel.eventInvalidPin.observe(viewLifecycleOwner, {
            it.getContentIfNotHandled()?.let { pinInvalid ->
                if (pinInvalid) {
                    onInvalidPin()
                }
            }
        })
        viewModel.eventPinMatch.observe(viewLifecycleOwner, {
            it.getContentIfNotHandled()?.let { pinsMatch ->
                if (!pinsMatch) {
                    onPinMismatch()
                }
            }
        })
        viewModel.eventLocalAccountCreated.observe(viewLifecycleOwner, { event ->
            event.getContentIfNotHandled()?.let {
                findNavController().navigate(it)
            }
        })

        return binding.root
    }

    private fun onInvalidPin() {
        view?.let {
            Snackbar.make(
                it,
                resources.getString(R.string.intro_pin_invalid_text),
                Snackbar.LENGTH_SHORT
            ).show()
        }
    }

    private fun onPinMismatch() {
        view?.let {
            Snackbar.make(
                it,
                resources.getString(R.string.intro_pin_mismatch_text),
                Snackbar.LENGTH_SHORT
            ).show()
        }
    }
}