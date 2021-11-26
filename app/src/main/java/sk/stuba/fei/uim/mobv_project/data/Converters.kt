package sk.stuba.fei.uim.mobv_project.data

import androidx.databinding.InverseMethod
import androidx.room.TypeConverter
import shadow.com.google.gson.Gson

object Converters {

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


    @TypeConverter
    fun strToAssetType(value: String) = enumValueOf<Constants.AssetType>(value)

    @TypeConverter
    fun assetTypeToString(assetType: Constants.AssetType) = assetType.name

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

    @TypeConverter
    fun listToJson(value: List<String>?): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun jsonToList(value: String): List<String>? {
        val objects = Gson().fromJson(value, Array<String>::class.java) as Array<String>
        return objects.toList()
    }
}