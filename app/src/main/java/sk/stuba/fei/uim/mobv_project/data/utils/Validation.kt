package sk.stuba.fei.uim.mobv_project.data.utils

import org.stellar.sdk.KeyPair
import org.stellar.sdk.Server
import org.stellar.sdk.responses.AccountResponse
import sk.stuba.fei.uim.mobv_project.data.exceptions.ValidationException
import java.lang.Exception
import kotlin.jvm.Throws


object Validation {

    fun validatePin(pin: String?): Boolean {
        return pin != null && 8 <= pin.length
    }

    @Throws(ValidationException::class)
    fun validateAccountId(accountId: String): KeyPair {
        try {
            return KeyPair.fromAccountId(accountId)
        } catch (e: Exception) {
            throw ValidationException("Invalid accountId")
        }
    }

    @Throws(ValidationException::class)
    fun validatePrivateKey(privateKey: String): KeyPair {
        try {
            return KeyPair.fromSecretSeed(privateKey)
        } catch (e: Exception) {
            throw ValidationException("Invalid private key")
        }
    }

    @Throws(ValidationException::class)
    fun doKeysMatch(accountId: String, privateKey: String): KeyPair {

        val accountIdKP = validateAccountId(accountId)
        val privateKeyKP = validatePrivateKey(privateKey)

        if (accountIdKP.accountId != privateKeyKP.accountId)
            throw ValidationException("Invalid accountId + privateKey combination")

        return privateKeyKP

    }

    @Throws(ValidationException::class)
    fun doesAccountExist(server: Server, accountId: String): AccountResponse {
        try {
            return server.accounts().account(accountId)
        } catch (e: Exception) {
            throw ValidationException("Account does not exist")
        }

    }

}