package sk.stuba.fei.uim.mobv_project.api

import android.annotation.SuppressLint
import android.content.Context
import org.stellar.sdk.*
import org.stellar.sdk.responses.AccountResponse
import sk.stuba.fei.uim.mobv_project.data.utils.Converters
import java.net.URL
import java.util.*
import org.stellar.sdk.responses.operations.PaymentOperationResponse
import org.stellar.sdk.responses.SubmitTransactionResponse
import sk.stuba.fei.uim.mobv_project.data.exceptions.*
import sk.stuba.fei.uim.mobv_project.data.utils.Validation
import kotlin.jvm.Throws


class StellarApi(private val context: Context) {

    private val server = Server(TESTNET_URL)

    companion object {
        const val TAG = "StellarApi"
        const val FRIENDBOT_URL = "https://friendbot.stellar.org"
        const val TESTNET_URL = "https://horizon-testnet.stellar.org"

        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var INSTANCE: StellarApi? = null
        fun getInstance(context: Context): StellarApi {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = StellarApi(context)
                    INSTANCE = instance
                }
                return instance
            }
        }
    }

    // posle sa request friendbotovi ktory nam da 10000 lumenov na acc
    @Suppress("BlockingMethodInNonBlockingContext")
    @Throws(TransactionFailedException::class)
    suspend fun createStellarAccount(accountId: String): Map<String?, Any?>? {
        val fundingUrl = "${FRIENDBOT_URL}/?addr=${accountId}"
        try {
            val stream = URL(fundingUrl).openStream()
            val jsonText = Scanner(stream, "UTF-8").useDelimiter("\\A").next()
            stream.close()
            return Converters.jsonToMap(jsonText)
        } catch (e: Exception) {
            throw TransactionFailedException("Unable to create a new account")
        }
    }

    // GET https://horizon-testnet.stellar.org/accounts/{accountId}
    @Suppress("BlockingMethodInNonBlockingContext")
    @Throws(ValidationException::class)
    suspend fun getStellarAccount(accountId: String): AccountResponse {
        return Validation.doesAccountExist(server, accountId)
    }

    // GET https://horizon-testnet.stellar.org/accounts/{accountId}/payments
    @Suppress("BlockingMethodInNonBlockingContext")
    @Throws(ValidationException::class, ApiException::class)
    suspend fun getStellarPayments(accountId: String): List<PaymentOperationResponse> {

        // check ci dava zmysel accountId
        val pair = Validation.validateAccountId(accountId)

        try {
            // get payments
            val paymentsRequest = server.payments().forAccount(pair.accountId).limit(100)
            val operations = paymentsRequest.execute().records

            val payments = ArrayList<PaymentOperationResponse>()
            operations.forEach { operation ->
                if (operation is PaymentOperationResponse)
                    payments.add(operation)
            }
            return payments
        } catch (e: Exception) {
            throw ApiException("Error while getting payments")
        }
    }

    @Throws(ValidationException::class, TransactionFailedException::class, ApiException::class)
    suspend fun sendStellarPayment(
        sourcePrivateKey: String,
        destinationPublicKey: String,
        amount: String,
        memo: String = "",
    ): SubmitTransactionResponse {
        // check ci kluce davaju zmysel
        val srcKeyPair = Validation.validatePrivateKey(sourcePrivateKey)
        val dstKeyPair = Validation.validateAccountId(destinationPublicKey)

        // check ci existuju accounty
        val sourceAccount = getStellarAccount(srcKeyPair.accountId)
        val destAccount = getStellarAccount(dstKeyPair.accountId)

        val response: SubmitTransactionResponse
        try {
            // posleme peniaze
            response =
                payment(srcKeyPair, sourceAccount, destAccount, AssetTypeNative(), amount, memo)

        } catch (e: Exception) {
            throw ApiException("Error while creating Payment transaction")
        }

        if (response.isSuccess) {
            return response
        } else {
            val reason = Converters.resultCodesToReason(response.extras.resultCodes)
            throw TransactionFailedException("Payment failed: $reason")
        }
    }


    @Suppress("BlockingMethodInNonBlockingContext")
    private suspend fun payment(
        srcKeyPair: KeyPair?,
        sourceAccount: AccountResponse?,
        destAccount: AccountResponse?,
        asset: Asset?,
        amount: String?,
        memo: String? = "",
    ): SubmitTransactionResponse {

        // vytvorenie paymentu
        val paymentOperation = PaymentOperation.Builder(
            destAccount?.accountId,
            asset,
            amount).build()

        // vytvorenie transakcie
        val transaction = Transaction.Builder(sourceAccount, Network.TESTNET)
            .addOperation(paymentOperation)
            .setTimeout(180)
            .setBaseFee(Transaction.MIN_BASE_FEE)
            .addMemo(Memo.text(memo))
            .build()

        // podpis
        transaction.sign(srcKeyPair)

        return server.submitTransaction(transaction)
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    @Throws(ValidationException::class,TransactionFailedException::class, ApiException::class)
    suspend fun changeTrust(
        asset: Asset, accountId: String, privateKey: String, limit: String = "1000",
    ): SubmitTransactionResponse {

        // check ci kluc dava zmysel
        val keyPair = Validation.validatePrivateKey(privateKey)
        // check ci existuje account
        val account = getStellarAccount(accountId)

        // build changeTrust transakciu
        val response: SubmitTransactionResponse
        try {
            val changeTrustOperation = ChangeTrustOperation.Builder(
                ChangeTrustAsset.Wrapper(asset), limit)
                .build()

            val transaction = Transaction.Builder(account, Network.TESTNET)
                .addOperation(changeTrustOperation)
                .setTimeout(180)
                .setBaseFee(Transaction.MIN_BASE_FEE)
                .build()

            transaction.sign(keyPair)
            response = server.submitTransaction(transaction)

        } catch (e: Exception) {
            throw ApiException("Error while creating Change Trust transaction")
        }

        if (response.isSuccess) {
            return response
        } else {
            val reason = Converters.resultCodesToReason(response.extras.resultCodes)
            throw TransactionFailedException("Change Trust failed: $reason")
        }

    }

}


