package sk.stuba.fei.uim.mobv_project.data.entities

import androidx.annotation.NonNull
import androidx.room.*

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
        "asset_code"//, "asset_issuer"
    ],
    indices = [
        Index(value = ["source_account"], unique = false),
        Index(value = ["asset_code"], unique = true),
    ]
)
data class Balances(

    @NonNull @ColumnInfo(name = "asset_code") var assetCode: String = "Lumens",
    @ColumnInfo(name = "balance") var balance: String?,
    @ColumnInfo(name = "limit") var limit: String?,
    // FK: ref na account
    @ColumnInfo(name = "source_account") var sourceAccount: String?,
    // ...
) : AppDbEntity()