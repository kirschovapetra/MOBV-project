package sk.stuba.fei.uim.mobv_project.data.entities

import androidx.room.*
import java.io.Serializable

@Entity(
    tableName = "contact",
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
data class Contact(
    @PrimaryKey @ColumnInfo(name = "contact_id") val contactId: String,
    @ColumnInfo(name = "name") val name: String?,
    // FK: ref na account
    @ColumnInfo(name = "source_account") val sourceAccount: String?
    // ...
) : Serializable