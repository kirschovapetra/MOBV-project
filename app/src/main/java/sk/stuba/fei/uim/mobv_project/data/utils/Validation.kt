package sk.stuba.fei.uim.mobv_project.data.utils

import org.stellar.sdk.KeyPair
import org.stellar.sdk.Server
import org.stellar.sdk.responses.AccountResponse
import sk.stuba.fei.uim.mobv_project.data.exceptions.ValidationException
import java.lang.Exception


object Validation {
    @JvmStatic
    fun validateAccountId(accountId: String): KeyPair {
        try {
            return KeyPair.fromAccountId(accountId)
        } catch (e: Exception) {
            throw ValidationException("Invalid accountId")
        }
    }

    @JvmStatic
    fun validatePrivateKey(privateKey: String): KeyPair {
        try {
            return KeyPair.fromSecretSeed(privateKey)
        } catch (e: Exception) {
            throw ValidationException("Invalid private key")
        }
    }

    @JvmStatic
    fun doKeysMatch(accountId: String, privateKey: String): KeyPair {

        val accountIdKP = validateAccountId(accountId)
        val privateKeyKP = validatePrivateKey(privateKey)

        if (accountIdKP.accountId != privateKeyKP.accountId)
            throw ValidationException("Invalid accountId + privateKey combination")

        return privateKeyKP

    }

    @JvmStatic
    fun doesAccountExist(server: Server, accountId: String): AccountResponse {
        try {
            return server.accounts().account(accountId)
        } catch (e: Exception) {
            throw ValidationException("Account does not exist")
        }

    }

}
/*
    case xdr.OperationResultCode:
		switch code {
		case xdr.OperationResultCodeOpInner:
			return "op_inner", nil
		case xdr.OperationResultCodeOpBadAuth:
			return "op_bad_auth", nil
		case xdr.OperationResultCodeOpNoAccount:
			return "op_no_source_account", nil

	case xdr.PaymentResultCode:
		switch code {
		case xdr.PaymentResultCodePaymentSuccess:
			return OpSuccess, nil
		case xdr.PaymentResultCodePaymentMalformed:
			return OpMalformed, nil
		case xdr.PaymentResultCodePaymentUnderfunded:
			return OpUnderfunded, nil
		case xdr.PaymentResultCodePaymentSrcNoTrust:
			return "op_src_no_trust", nil
		case xdr.PaymentResultCodePaymentSrcNotAuthorized:
			return "op_src_not_authorized", nil
		case xdr.PaymentResultCodePaymentNoDestination:
			return "op_no_destination", nil
		case xdr.PaymentResultCodePaymentNoTrust:
			return "op_no_trust", nil
		case xdr.PaymentResultCodePaymentNotAuthorized:
			return "op_not_authorized", nil
		case xdr.PaymentResultCodePaymentLineFull:
			return OpLineFull, nil
		case xdr.PaymentResultCodePaymentNoIssuer:
			return OpNoIssuer, nil
		}

			// OpSuccess is the string code used to specify the operation was successful
	OpSuccess = "op_success"
	// OpMalformed is the string code used to specify the operation was malformed
	// in some way.
	OpMalformed = "op_malformed"
	// OpUnderfunded is the string code used to specify the operation failed
	// due to a lack of funds.
	OpUnderfunded = "op_underfunded"

	// OpLowReserve is the string code used to specify the operation failed
	// because the account in question does not have enough balance to satisfy
	// what their new minimum balance would be.
	OpLowReserve = "op_low_reserve"

	// OpLineFull occurs when a payment would cause a destination account to
	// exceed their declared trust limit for the asset being sent.
	OpLineFull = "op_line_full"

	// OpNoIssuer occurs when a operation does not correctly specify an issuing
	// asset
	OpNoIssuer = "op_no_issuer"
*/