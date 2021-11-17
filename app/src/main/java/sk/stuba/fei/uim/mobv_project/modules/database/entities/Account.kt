package sk.stuba.fei.uim.mobv_project.modules.database.entities

import androidx.room.*
import java.util.*

@Entity(tableName = "account")
data class Account(
    @PrimaryKey @ColumnInfo(name = "account_id") val accountId: String,
    @ColumnInfo(name = "first_name") val firstName: String?,
    @ColumnInfo(name = "last_name") val lastName: String?,
//    @ColumnInfo(name = "pin") val pin: String?, // ?
    @ColumnInfo(name = "sequence") val sequence: String?, // ?
    @ColumnInfo(name = "last_modified_ledger") val lastModifiedLedger: Int?, // ?
    @ColumnInfo(name = "last_modified_time") val lastModifiedTime: Date?, // ?
    // + dalsie veci ak by bolo treba
    )

