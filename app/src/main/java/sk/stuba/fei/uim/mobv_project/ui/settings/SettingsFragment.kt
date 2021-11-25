package sk.stuba.fei.uim.mobv_project.ui.settings
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import sk.stuba.fei.uim.mobv_project.R
import sk.stuba.fei.uim.mobv_project.ui.abstracts.NoNavigationUpFragment

class SettingsFragment : NoNavigationUpFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }
}