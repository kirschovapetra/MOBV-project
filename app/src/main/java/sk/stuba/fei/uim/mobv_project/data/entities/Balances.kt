package sk.stuba.fei.uim.mobv_project.data.entities

import androidx.room.*
import sk.stuba.fei.uim.mobv_project.data.Constants

@Entity(
    tableName = "balances",
    foreignKeys = [
        ForeignKey(
            entity = Account::class,
            parentColumns = ["account_id"],
            childColumns = ["source_account"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value= ["source_account" ], unique= true)
    ]
)
data class Balances(

    @ColumnInfo(name = "balance") val balance: Double?,
    @ColumnInfo(name = "limit") val limit: Double?,
    @ColumnInfo(name = "asset_type") val assetType: Constants.AssetType?,
    // FK: ref na account
    @ColumnInfo(name = "source_account") val sourceAccount: String?,
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "balance_id") val balanceId: Long = 0L,
    // ...
)
