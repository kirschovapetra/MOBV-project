package sk.stuba.fei.uim.mobv_project.api

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import org.stellar.sdk.Server
import org.stellar.sdk.responses.AccountResponse
import sk.stuba.fei.uim.mobv_project.data.Converters
import java.net.URL
import java.util.*

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

    fun getStellarAccount(accountId: String): AccountResponse? {
        return server.accounts().account(accountId)
//        Log.i("BALANCE", "BALANCES FOR ACCOUNT: ${pair.accountId}")
//        for (balance in account.balances) {
//            Log.i("BALANCE",
//                "Type: ${balance.assetType}, " +
//                        "Code: ${balance.assetCode}, " +
//                        "Balance: ${balance.balance}")
//        }
    }


}




