package sk.stuba.fei.uim.mobv_project.data

import androidx.room.TypeConverter

class Converters {

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

}