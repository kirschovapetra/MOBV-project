package sk.stuba.fei.uim.mobv_project.data.exceptions

import java.lang.IllegalArgumentException

class ValidationException(override val message: String?): IllegalArgumentException(message) {}