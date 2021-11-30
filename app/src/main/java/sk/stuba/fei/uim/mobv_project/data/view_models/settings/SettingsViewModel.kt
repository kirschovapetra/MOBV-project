package sk.stuba.fei.uim.mobv_project.data.view_models.settings

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import sk.stuba.fei.uim.mobv_project.data.entities.Contact
import sk.stuba.fei.uim.mobv_project.data.exceptions.ApiException
import sk.stuba.fei.uim.mobv_project.data.exceptions.TransactionFailedException
import sk.stuba.fei.uim.mobv_project.data.exceptions.ValidationException
import sk.stuba.fei.uim.mobv_project.data.repositories.AccountRepository
import sk.stuba.fei.uim.mobv_project.data.repositories.BalanceRepository
import sk.stuba.fei.uim.mobv_project.data.repositories.ContactRepository
import sk.stuba.fei.uim.mobv_project.data.repositories.PaymentRepository
import java.lang.Exception


class SettingsViewModel(
    private val accountRepo: AccountRepository,
    private val balanceRepo: BalanceRepository,
    private val contactRepo: ContactRepository,
    private val paymentRepo: PaymentRepository,
) : ViewModel() {

    // tento ma trusted native a dogecoin
    val senderPublic = "GAL7TYW3AMA5PNK37FBA4BRN44OJDLEW42G5EZZ4TIHT5HX5SIQZD33J"
    val senderSecret = "SC35HCUFX5NF3TEDP2P5D4PVCUT4MHK5KMMGSDHQYQHRABEYMOEOX3ZI"

    // tento ma trusted native, dogecoin, usd
    // ale neviem jeho privateKey takze ten je iba kontakt
    val contactPublic = "GAVDLPPKCQLKPDP5N3LZXJVJGMJ7VRX2LLTKZO6R62EG53A3EZP3GH53"

    val dogecoinAssetCode = "DOGECOIN"
    val dogecoinAssetIssuer = "GB46LWKQW65CZ7AMEY7JD4Z4ADX2CA4XYQBAJK6VTIUP6MWQUKCRQDJF"

    // "asset_code": "DOGECOIN", "asset_issuer": "GB46LWKQW65CZ7AMEY7JD4Z4ADX2CA4XYQBAJK6VTIUP6MWQUKCRQDJF"
    // "asset_code": "USD", "asset_issuer": "GA234B7P5ME6FTFLZ77PBYMRUJ3L33I6UPNYZ3XAUY4NNT7QWIAV7GL5"

    fun insertAccountToDb() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    accountRepo.syncAccount("Jozko", "Mrkvicka", "1234", senderSecret)

                    // treba syncnut api s db pre kazdu tabulku zvlast
                    balanceRepo.syncBalances(senderPublic)
                    paymentRepo.syncPayments(senderPublic)

                    contactRepo.insertContact(
                        Contact(contactPublic, "Moj najlepsi kamko", senderPublic)
                    )

                    // native platba - v Lumenoch
//                paymentRepo.sendAndSyncPayment(
//                    sourcePublicKey = senderPublic, sourcePrivateKey = senderSecret,
//                    destinationPublicKey = contactPublic, amount = "50", memo = "Serus bruh")

                    // custom platba - v dogecoinoch (jediny rozdiel ze sa zada assetCode a issuer)
                    // failne lebo nemame ziadne dogecoiny :(
                    paymentRepo.sendAndSyncPayment(
                        sourcePublicKey = senderPublic, sourcePrivateKey = senderSecret,
                        destinationPublicKey = contactPublic, assetCode = dogecoinAssetCode,
                        assetIssuer = dogecoinAssetIssuer, amount = "100", memo = "tak nic")

                    balanceRepo.syncBalances(senderPublic)
                    paymentRepo.syncPayments(senderPublic)
                } catch (e: ValidationException) {
                    Log.e("SettingsViewModel", "ValidationException: ${e.message}")
                } catch (e: TransactionFailedException) {
                    Log.e("SettingsViewModel", "TransactionFailedException: ${e.message}")
                } catch (e: ApiException) {
                    Log.e("SettingsViewModel", "ApiException: ${e.message}")
                } catch (e: Exception) {
                    Log.e("SettingsViewModel", "Nieco sa ale ze brutalne posralo ${e.message}")
                }
            }
        }
    }

    fun clearDatabase() {
        viewModelScope.launch {
            accountRepo.clearAccounts()
            balanceRepo.clearBalances()
            contactRepo.clearContacts()
            paymentRepo.clearPayments()

            Log.i("SettingsViewModel", "Databaza je vymazana")
        }
    }
}