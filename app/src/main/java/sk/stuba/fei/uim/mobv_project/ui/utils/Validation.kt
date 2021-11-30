package sk.stuba.fei.uim.mobv_project.ui.utils

object Validation {
    fun validatePin(pin: String?): Boolean {
        return pin != null && 8 <= pin.length
    }
}