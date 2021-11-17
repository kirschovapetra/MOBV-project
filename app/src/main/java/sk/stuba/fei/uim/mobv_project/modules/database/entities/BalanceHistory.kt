package sk.stuba.fei.uim.mobv_project.modules.database.entities

import androidx.room.*
import sk.stuba.fei.uim.mobv_project.modules.general.constants.AssetType

@Entity(
    tableName = "balance_history",
    foreignKeys = [
        ForeignKey(
            entity = Account::class,
            parentColumns = ["accountId"],
            childColumns = ["sourceAccount"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class BalanceHistory(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "balance_id") val balanceId: String,
    @ColumnInfo(name = "balance") val balance: Double?,
    @ColumnInfo(name = "limit") val limit: Double?,
    @ColumnInfo(name = "asset_type") val assetType: AssetType?,
    // FK: ref na account
    @ColumnInfo(name = "source_account") val sourceAccount: String?,
    // ...
)
