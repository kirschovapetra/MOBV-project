package sk.stuba.fei.uim.mobv_project.data.exceptions

import java.lang.Exception

class TransactionFailedException(override val message: String?): Exception(message) {}