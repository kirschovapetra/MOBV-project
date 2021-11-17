package sk.stuba.fei.uim.mobv_project.modules.database.entities

import androidx.room.*
import java.util.*
import sk.stuba.fei.uim.mobv_project.modules.general.constants.AssetType

@Entity(
    tableName = "payment",
    foreignKeys = [
        androidx.room.ForeignKey(
            entity = Account::class,
            parentColumns = ["accountId"],
            childColumns = ["sourceAccount"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Payment(
    @PrimaryKey @ColumnInfo(name = "payment_id") val paymentId: String,
    @ColumnInfo(name = "transaction_hash") val transactionHash: String?,
    @ColumnInfo(name = "transaction_successful") val transactionSuccessful: Boolean?,
    @ColumnInfo(name = "created_at") val createdAt: Date?,
    @ColumnInfo(name = "asset_type") val assetType: AssetType?,
    @ColumnInfo(name = "from") val from: String?,
    @ColumnInfo(name = "to") val to: String?,
    @ColumnInfo(name = "amount") val amount: Double?,
    // FK: ref na account
    @ColumnInfo(name = "source_account") val sourceAccount: String?
    // ...
)
