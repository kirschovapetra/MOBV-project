package sk.stuba.fei.uim.mobv_project.ui.utils

import android.view.View
import com.google.android.material.snackbar.Snackbar
import sk.stuba.fei.uim.mobv_project.data.view_models.event.Event

object NotificationUtils {
    fun showSnackbarFromResourceEvent(view: View?, resourceIdEvent: Event<Int?>, duration: Int) {
        resourceIdEvent.getContentIfNotHandled()?.let {
            showSnackbar(view, it, duration)
        }
    }

    fun showSnackbarFromMessageEvent(view: View?, message: Event<String?>, duration: Int) {
        message.getContentIfNotHandled()?.let {
            showSnackbar(view, it, duration)
        }
    }

    fun showSnackbar(view: View?, resourceId: Int, duration: Int) {
        view?.let {
            Snackbar.make(it, resourceId, duration)
                .setAction("X") {
                    it.visibility = View.GONE
                }
                .show()
        }
    }

    fun showSnackbar(view: View?, message: String, duration: Int) {
        view?.let {
            Snackbar.make(it, message, duration)
                .setAction("X") {
                    it.visibility = View.GONE
                }
                .show()
        }
    }

    fun showAnchorSnackbar(view: View?, message: String, duration: Int, anchorId: Int) {
        view?.let {
            Snackbar.make(it, message, duration).setAnchorView(anchorId)
                .setAction("X") {
                    it.visibility = View.GONE
                }
                .show()
        }
    }

    fun showAnchorSnackbar(view: View?, resourceId: Int, duration: Int, anchorId: Int) {
        view?.let {
            Snackbar.make(it, resourceId, duration).setAnchorView(anchorId)
                .setAction("X") {
                    it.visibility = View.GONE
                }
                .show()
        }
    }
}