package sk.stuba.fei.uim.mobv_project.ui.intro

import androidx.fragment.app.Fragment
import sk.stuba.fei.uim.mobv_project.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import sk.stuba.fei.uim.mobv_project.databinding.FragmentIntroBinding

class IntroFragment : Fragment(R.layout.fragment_intro) {
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        binding!!.createNewWalletButton.setOnClickListener {
//            findNavController().navigate(
//                IntroFragmentDirections.actionIntroFragmentToCreateWalletFragment()
//            )
//        }
//        binding!!.importWalletButton.setOnClickListener {
//            findNavController().navigate(
//                IntroFragmentDirections.actionIntroFragmentToImportWalletFragment()
//            )
//        }
//    }
    private lateinit var binding: FragmentIntroBinding
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
        return binding.root
    }
}