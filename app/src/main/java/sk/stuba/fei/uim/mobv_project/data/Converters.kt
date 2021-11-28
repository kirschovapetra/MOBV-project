package sk.stuba.fei.uim.mobv_project.data

import androidx.databinding.InverseMethod
import org.stellar.sdk.Asset
import org.stellar.sdk.AssetTypeCreditAlphaNum
import org.stellar.sdk.AssetTypeNative
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
//
// z api by (asi) mali prist datumy ako string takze teraz toto az tak netreba.
// ale mozno sa zide do buducna
//    @TypeConverter
//    fun timestampToDate(value: Long?): Date? {
//        return value?.let { Date(it) }
//    }
//
//    @TypeConverter
//    fun dateToTimestamp(date: Date?): Long? {
//        return date?.time?.toLong()
//    }

}