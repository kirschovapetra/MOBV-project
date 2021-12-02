package sk.stuba.fei.uim.mobv_project.ui.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.View
import com.google.android.material.snackbar.Snackbar
import sk.stuba.fei.uim.mobv_project.data.view_models.event.Event

object ClipboardUtils {
    fun copyToClipboard(
        context: Context?,
        label: String,
        content: Event<String?>,
        view: View? = null,
        messageResourceId: Int? = null
    ) {
        content.getContentIfNotHandled()?.let {
            copyToClipboard(context, label, content, view, messageResourceId)
        }
    }

    fun copyToClipboard(
        context: Context?,
        label: String,
        content: String?,
        view: View? = null,
        messageResourceId: Int? = null
    ) {
        val copiedToClipboard = copyToClipboard(context, label, content)

        if (copiedToClipboard && messageResourceId != null) {
            NotificationUtils.showSnackbar(view, messageResourceId, Snackbar.LENGTH_LONG)
        }
    }

    fun copyToClipboard(context: Context?, label: String, content: String?): Boolean {
        return if (context != null && content != null) {
            val clipboardManager =
                context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData: ClipData = ClipData.newPlainText(label, content)

            clipboardManager.setPrimaryClip(clipData)
            true
        } else {
            false
        }
    }
}