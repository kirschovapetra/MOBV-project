package sk.stuba.fei.uim.mobv_project.ui.intro

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import sk.stuba.fei.uim.mobv_project.R
import sk.stuba.fei.uim.mobv_project.databinding.FragmentIntroBinding
import sk.stuba.fei.uim.mobv_project.ui.abstracts.FullscreenFragment

class IntroFragment : FullscreenFragment<FragmentIntroBinding>(
    R.layout.fragment_intro,
    FragmentIntroBinding::bind
) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding!!.createNewWalletButton.setOnClickListener {
            findNavController().navigate(
                IntroFragmentDirections.actionIntroFragmentToCreateWalletFragment()
            )
        }
        binding!!.importWalletButton.setOnClickListener {
            findNavController().navigate(
                IntroFragmentDirections.actionIntroFragmentToImportWalletFragment()
            )
        }
    }
}