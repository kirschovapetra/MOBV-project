package sk.stuba.fei.uim.mobv_project.data.entities

import androidx.room.*

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
        Index(value = ["source_account"], unique = false),
        Index(value = ["payment_id"], unique = true)
    ]
)
data class Payment(
    @PrimaryKey @ColumnInfo(name = "payment_id") var paymentId: Long = -1L,
    @ColumnInfo(name = "transaction_hash") var transactionHash: String? = "",
    @ColumnInfo(name = "transaction_successful") var transactionSuccessful: Boolean?,
    @ColumnInfo(name = "created_at") var createdAt: String? = "",
    @ColumnInfo(name = "asset_code") var assetCode: String? = "Lumens",
    @ColumnInfo(name = "from") var from: String? = "",
    @ColumnInfo(name = "to") var to: String? = "",
    @ColumnInfo(name = "amount") var amount: String? = "",
    // FK: ref na account
    @ColumnInfo(name = "source_account") var sourceAccount: String? = ""
) : AppDbEntity() {
    constructor() : this(transactionSuccessful = null)
}