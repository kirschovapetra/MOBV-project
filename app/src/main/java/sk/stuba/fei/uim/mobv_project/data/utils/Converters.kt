package sk.stuba.fei.uim.mobv_project.data.utils

import androidx.databinding.InverseMethod
import org.stellar.sdk.Asset
import org.stellar.sdk.AssetTypeCreditAlphaNum
import org.stellar.sdk.AssetTypeNative
import org.stellar.sdk.responses.SubmitTransactionResponse
import shadow.com.google.gson.Gson
import shadow.com.google.gson.internal.LinkedTreeMap

object Converters {

    @JvmStatic fun stringToFloat(value: String?): Float?{
        if(value == null) {
            return 0.0f
        }
        return value.toFloat()
    }
    @InverseMethod("stringToFloat")
    @JvmStatic fun floatToString(value: Float?): String{
        if(value == null)
            return ""
        return value.toString()
    }

    @JvmStatic fun jsonToMap(jsonText: String?): Map<String?, Any?>?{
        val map: Map<String?, Any?> = LinkedTreeMap()
        return Gson().fromJson(jsonText, map.javaClass)
    }

    @JvmStatic fun objectToJson(obj: Any?): String?{
        return Gson().toJson(obj)
    }

    @JvmStatic fun assetToAssetCode(asset: Asset?): String {
        var assetCode = "Lumens"

        if (asset != null && asset != AssetTypeNative()) {
            val tmpAsset = asset as AssetTypeCreditAlphaNum
            assetCode = "${tmpAsset.code}:${tmpAsset.issuer}"
        }
        return assetCode
    }

    @JvmStatic
    fun resultCodesToReason(resultCodes: SubmitTransactionResponse.Extras.ResultCodes): String {

        val opCodes = resultCodes.operationsResultCodes
        val results = ArrayList<String>()

        for (opCode in opCodes) {
            when (opCode) {
                "op_no_source_account" -> results.add("You did not specify the source account")
                "op_no_destination" -> results.add("You did not specify the destination account")
                "op_src_no_trust" -> results.add("Source account does not trust this asset")
                "op_no_trust" -> results.add("Destination account does not trust this asset")
                "op_src_not_authorized" -> results.add("Could not authorize the source account")
                "op_not_authorized" -> results.add("Could not authorize the destination account")
                "op_underfunded" -> results.add("You do not have enough balance")
                "op_no_issuer" -> results.add("You did not specify the asset issuer")
                else -> {
                    continue
                }
            }
        }
        return if (results.isEmpty()) "Other failure" else results.joinToString(", ")
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


//    TODO odkomentujem ak to bude treba
//    @TypeConverter
//    fun strToAssetType(value: String) = enumValueOf<Constants.AssetType>(value)
//
//    @TypeConverter
//    fun assetTypeToString(assetType: Constants.AssetType) = assetType.name

//    @TypeConverter
//    fun listToJson(value: List<String>?): String {
//        return Gson().toJson(value)
//    }
//
//    @TypeConverter
//    fun jsonToList(value: String): List<String> {
//        val objects = Gson().fromJson(value, Array<String>::class.java) as Array<String>
//        return objects.toList()
//    }

}