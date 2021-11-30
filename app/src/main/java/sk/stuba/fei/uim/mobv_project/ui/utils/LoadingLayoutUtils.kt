package sk.stuba.fei.uim.mobv_project.ui.utils

import android.app.Activity
import android.view.View
import android.widget.RelativeLayout
import androidx.transition.Fade
import androidx.transition.TransitionManager
import sk.stuba.fei.uim.mobv_project.R

object LoadingLayoutUtils {
    fun setLoadingLayoutVisibility(activity: Activity?, visible: Boolean) {
        activity?.let {
            val loadinPanel = it.findViewById<RelativeLayout>(R.id.loadingPanel)
            val transition = Fade()
            transition.duration = 200
            transition.addTarget(loadinPanel)

            TransitionManager.beginDelayedTransition(loadinPanel, transition)

            if (visible) {
                loadinPanel.visibility = View.VISIBLE
            }
            else {
                loadinPanel.visibility = View.GONE
            }
        }
    }
}