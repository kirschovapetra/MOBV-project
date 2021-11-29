package sk.stuba.fei.uim.mobv_project.data.exceptions

import java.lang.Exception

class ApiException(override val message: String?): Exception(message) {}