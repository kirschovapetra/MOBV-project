package sk.stuba.fei.uim.mobv_project.data.entities

import androidx.room.*
import androidx.room.ForeignKey.CASCADE


// optional, iba ak by sme chceli kreslit graf

@Entity(
    tableName = "balance_history",
    foreignKeys = [
        ForeignKey(
            entity = Balances::class,
            parentColumns = ["asset_code", "asset_issuer"],
            childColumns = ["asset_code", "asset_issuer"],
            onDelete = CASCADE
        ),
    ],
    indices = [
        Index(value = ["asset_code", "asset_issuer"], unique = true)
    ]
)
data class BalanceHistory(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "balance_id") val balanceId: Long,
    // FK: ref na asset_code
    @ColumnInfo(name = "asset_code") val assetCode: String?,
    // FK: ref na asset_issuer
    @ColumnInfo(name = "asset_issuer") val assetIssuer: String?,
    @ColumnInfo(name = "balance") val balance: Double?,
)