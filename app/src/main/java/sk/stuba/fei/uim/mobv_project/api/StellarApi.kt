package sk.stuba.fei.uim.mobv_project.api

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import org.stellar.sdk.KeyPair
import org.stellar.sdk.Server
import org.stellar.sdk.responses.AccountResponse
import sk.stuba.fei.uim.mobv_project.data.Converters
import java.net.URL
import java.util.*
import org.stellar.sdk.responses.operations.PaymentOperationResponse

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

    // accountId == private key
    // posle sa request friendbotovi ktory nam da 10000 lumenov na acc
    fun createStellarAccount(accountId: String): Map<String?, Any?>? {
        val fundingUrl = "${FRIENDBOT_URL}/?addr=${accountId}"
        val stream = URL(fundingUrl).openStream()

        try {
            val jsonText = Scanner(stream, "UTF-8").useDelimiter("\\A").next()
            return Converters.jsonToMap(jsonText)
        } catch (e: Exception) {
            Log.e("CREATE_ACCOUNT", e.message!!)
        } finally {
            stream.close()
        }
        return null
    }

    // GET https://horizon-testnet.stellar.org/accounts/{accountId}
    fun getStellarAccount(accountId: String): AccountResponse? {
        return server.accounts().account(accountId)
    }

    // GET https://horizon-testnet.stellar.org/accounts/{accountId}/payments
    fun getStellarPayments(accountId: String): List<PaymentOperationResponse>? {
        val pair = KeyPair.fromAccountId(accountId)
        val paymentsRequest = server.payments().forAccount(pair.accountId)
        try {
            val operations = paymentsRequest.execute().records
            var payments = ArrayList<PaymentOperationResponse>()
            operations.forEach { operation ->
                if (operation is PaymentOperationResponse)
                    payments.add(operation)
//                else
//                    Log.i("GET_PAYMENTS", "Nasla sa operacia ${operation.type}")
            }
            Log.i("GET_PAYMENTS",
                if (payments.isEmpty()) "Nenasla sa ziadna platba pre $accountId"
                else "Success $accountId")

            return payments
        } catch (e: Exception) {
            Log.e("GET_PAYMENTS", e.message!!)
        }
        return null
    }
}


