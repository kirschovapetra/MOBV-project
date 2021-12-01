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
}