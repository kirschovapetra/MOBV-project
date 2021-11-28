package sk.stuba.fei.uim.mobv_project.api

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import org.stellar.sdk.*
import org.stellar.sdk.responses.AccountResponse
import sk.stuba.fei.uim.mobv_project.data.Converters
import java.net.URL
import java.util.*
import org.stellar.sdk.responses.operations.PaymentOperationResponse
import org.stellar.sdk.responses.SubmitTransactionResponse

// TODO krajsejsie logy a error handling


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
    suspend fun createStellarAccount(accountId: String): Map<String?, Any?>? {
        val fundingUrl = "${FRIENDBOT_URL}/?addr=${accountId}"
        val stream = URL(fundingUrl).openStream()
        val jsonText = Scanner(stream, "UTF-8").useDelimiter("\\A").next()
        return Converters.jsonToMap(jsonText)
    }

    // GET https://horizon-testnet.stellar.org/accounts/{accountId}
    @Suppress("BlockingMethodInNonBlockingContext")
    suspend fun getStellarAccount(accountId: String): AccountResponse? {
        return server.accounts().account(accountId)
    }

    // GET https://horizon-testnet.stellar.org/accounts/{accountId}/payments
    @Suppress("BlockingMethodInNonBlockingContext")
    suspend fun getStellarPayments(accountId: String): List<PaymentOperationResponse>? {

        // check ci dava zmysel accountId
        val pair = KeyPair.fromAccountId(accountId)

        // get paymenty
        val paymentsRequest = server.payments().forAccount(pair.accountId)
        val operations = paymentsRequest.execute().records

        val payments = ArrayList<PaymentOperationResponse>()
        operations.forEach { operation ->
            if (operation is PaymentOperationResponse)
                payments.add(operation)
        }
        return payments
    }

    suspend fun sendStellarTransaction(
        sourcePrivateKey: String,
        destinationPublicKey: String,
        assetCode: String = "Lumens",
        assetIssuer: String = "",
        amount: String,
        memo: String = "",
    ): SubmitTransactionResponse {

        return if (assetCode == "Lumens") {
            sendNativeTransaction(
                sourcePrivateKey, destinationPublicKey, amount, memo)
        } else {
            sendCustomAssetTransaction(
                sourcePrivateKey, destinationPublicKey, assetCode, assetIssuer, amount, memo)
        }
    }

    private suspend fun sendNativeTransaction(
        sourcePrivateKey: String,
        destinationPublicKey: String,
        amount: String,
        memo: String = "",
    ): SubmitTransactionResponse {
        // check ci kluce davaju zmysel
        val srcKeyPair = KeyPair.fromSecretSeed(sourcePrivateKey)
        val dstKeyPair = KeyPair.fromAccountId(destinationPublicKey)

        // check ci existuju accounty
        val sourceAccount = getStellarAccount(srcKeyPair.accountId)
        val destAccount = getStellarAccount(dstKeyPair.accountId)

        // posleme peniaze
        val response =
            payment(srcKeyPair, sourceAccount, destAccount, AssetTypeNative(), amount, memo)

        if (response.isSuccess) {
            return response
        } else {
            val rc = response.extras.resultCodes
            throw Exception("Payment transaction failed: " +
                    "${rc.transactionResultCode} ${rc.operationsResultCodes}")
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

        // TODO este niekde medzi tym check aj s pinom

        // podpis
        transaction.sign(srcKeyPair)

        return server.submitTransaction(transaction)
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    suspend fun changeTrust(
        asset: Asset, accountId: String, privateKey: String, limit: String = "1000",
    ): SubmitTransactionResponse {

        // check ci kluc dava zmysel
        val keyPair = KeyPair.fromSecretSeed(privateKey)
        // check ci existuje account
        val account = getStellarAccount(accountId)

        // build changeTrust transakcie
        val changeTrustOperation = ChangeTrustOperation.Builder(
            ChangeTrustAsset.Wrapper(asset), limit)
            .build()

        val transaction = Transaction.Builder(account, Network.TESTNET)
            .addOperation(changeTrustOperation)
            .setTimeout(180)
            .setBaseFee(Transaction.MIN_BASE_FEE)
            .build()

        transaction.sign(keyPair)

        val response = server.submitTransaction(transaction)

        if (response.isSuccess) {
            return response
        } else {
            val rc = response.extras.resultCodes
            throw Exception("ChangeTrust transaction failed: " +
                    "${rc.transactionResultCode} ${rc.operationsResultCodes}")

        }

    }


    private suspend fun sendCustomAssetTransaction(
        sourcePrivateKey: String,
        destinationPublicKey: String,
        assetCode: String,
        assetIssuer: String,
        amount: String,
        memo: String = "",
    ): SubmitTransactionResponse {

        // check ci kluce davaju zmysel
        val srcKeyPair = KeyPair.fromSecretSeed(sourcePrivateKey)
        val dstKeyPair = KeyPair.fromAccountId(destinationPublicKey)

        // check ci existuju accounty
        val sourceAccount = getStellarAccount(srcKeyPair.accountId)
        val destAccount = getStellarAccount(dstKeyPair.accountId)

        // posleme peniaze v assetovej mene
        val asset = Asset.createNonNativeAsset(assetCode, assetIssuer) as AssetTypeCreditAlphaNum
        val response = payment(srcKeyPair, sourceAccount, destAccount, asset, amount, memo)

        if (response.isSuccess) {
            return response
        } else {
            val rc = response.extras.resultCodes
            throw Exception("Payment transaction failed: " +
                    "${rc.transactionResultCode} ${rc.operationsResultCodes}")
        }
    }

}


