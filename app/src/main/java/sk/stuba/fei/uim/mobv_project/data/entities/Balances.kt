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
        "asset_code", "source_account"
    ],
    indices = [
        Index(value = ["asset_code", "source_account"], unique = true),
        Index(value = ["source_account"], unique = false),
    ]
)
data class Balances(

    @NonNull @ColumnInfo(name = "asset_code") var assetCode: String = "Lumens",
    @NonNull @ColumnInfo(name = "source_account") var sourceAccount: String = "", // FK: ref na account
    @ColumnInfo(name = "balance") var balance: String? = "",
) : AppDbEntity()