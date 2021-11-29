package sk.stuba.fei.uim.mobv_project.data.utils

import org.stellar.sdk.KeyPair
import org.stellar.sdk.Server
import org.stellar.sdk.responses.AccountResponse
import sk.stuba.fei.uim.mobv_project.data.exceptions.ValidationException
import java.lang.Exception
import kotlin.jvm.Throws


object Validation {
    @JvmStatic
    @Throws(ValidationException::class)
    fun validateAccountId(accountId: String): KeyPair {
        try {
            return KeyPair.fromAccountId(accountId)
        } catch (e: Exception) {
            throw ValidationException("Invalid accountId")
        }
    }

    @JvmStatic
    @Throws(ValidationException::class)
    fun validatePrivateKey(privateKey: String): KeyPair {
        try {
            return KeyPair.fromSecretSeed(privateKey)
        } catch (e: Exception) {
            throw ValidationException("Invalid private key")
        }
    }

    @JvmStatic
    @Throws(ValidationException::class)
    fun doKeysMatch(accountId: String, privateKey: String): KeyPair {

        val accountIdKP = validateAccountId(accountId)
        val privateKeyKP = validatePrivateKey(privateKey)

        if (accountIdKP.accountId != privateKeyKP.accountId)
            throw ValidationException("Invalid accountId + privateKey combination")

        return privateKeyKP

    }

    @JvmStatic
    @Throws(ValidationException::class)
    fun doesAccountExist(server: Server, accountId: String): AccountResponse {
        try {
            return server.accounts().account(accountId)
        } catch (e: Exception) {
            throw ValidationException("Account does not exist")
        }

    }

}