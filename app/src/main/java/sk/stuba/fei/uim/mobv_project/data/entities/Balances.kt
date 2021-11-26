package sk.stuba.fei.uim.mobv_project.data.entities

import androidx.annotation.NonNull
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
    primaryKeys = [
        "asset_code", "asset_issuer"
    ],
    indices = [
        Index(value= ["source_account" ], unique= true)
    ]
)
data class Balances(

    @NonNull @ColumnInfo(name = "asset_code") val assetCode: String = "native", // default val.
    @NonNull @ColumnInfo(name = "asset_issuer") val assetIssuer: String,
    @ColumnInfo(name = "asset_type") val assetType: Constants.AssetType?,
    @ColumnInfo(name = "balance") val balance: Double?,
    @ColumnInfo(name = "limit") val limit: Double?,
    // FK: ref na account
    @ColumnInfo(name = "source_account") val sourceAccount: String?,
    // ...
) : AppDbEntity()