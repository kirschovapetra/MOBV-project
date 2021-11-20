package sk.stuba.fei.uim.mobv_project.data.entities

import androidx.room.*
import sk.stuba.fei.uim.mobv_project.data.Constants.AssetType

@Entity(
    tableName = "payment",
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
data class Payment(
    @PrimaryKey @ColumnInfo(name = "payment_id") val paymentId: String,
    @ColumnInfo(name = "transaction_hash") val transactionHash: String?,
    @ColumnInfo(name = "transaction_successful") val transactionSuccessful: Boolean?,
    @ColumnInfo(name = "created_at") val createdAt: String?,
    @ColumnInfo(name = "asset_type") val assetType: AssetType = AssetType.native,
    @ColumnInfo(name = "asset_code") val assetCode: String = "native",
    @ColumnInfo(name = "asset_issuer") val assetIssuer: String = "native",
    @ColumnInfo(name = "from") val from: String?,
    @ColumnInfo(name = "to") val to: String?,
    @ColumnInfo(name = "amount") val amount: Double?,
    // FK: ref na account
    @ColumnInfo(name = "source_account") val sourceAccount: String?
    // ...
)