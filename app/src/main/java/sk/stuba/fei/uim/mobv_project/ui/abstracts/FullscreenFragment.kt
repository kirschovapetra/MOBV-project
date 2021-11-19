package sk.stuba.fei.uim.mobv_project.ui.abstracts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class FullscreenFragment<T : ViewBinding>(
    private val layoutId: Int,
    private val bindingFunction: (view: View) -> T
) : Fragment() {

    protected var binding: T? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(layoutId, container, false)
        binding = bindingFunction.invoke(view)

        return view
    }

    // clear references to binding
    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    //TODO: don't hide or fix flickering??? issue
    // hide and show action bar
    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar?.hide()
    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity).supportActionBar?.show()
    }

}