package sk.stuba.fei.uim.mobv_project.ui.utils

import android.annotation.SuppressLint
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
            Snackbar.make(it, resourceId, duration).show()
        }
    }

    fun showSnackbar(view: View?, message: String, duration: Int) {
        view?.let {
            Snackbar.make(it, message, duration).show()
        }
    }

    fun showAnchorSnackbar(view: View?, message: String, duration: Int, anchorId: Int) {
        view?.let {
            Snackbar.make(it, message, duration).setAnchorView(anchorId).show()
        }
    }
}