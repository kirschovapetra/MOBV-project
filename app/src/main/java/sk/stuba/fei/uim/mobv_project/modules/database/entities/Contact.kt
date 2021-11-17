package sk.stuba.fei.uim.mobv_project.modules.database.entities

import androidx.room.*

@Entity(
    tableName = "contact",
    foreignKeys = [
        ForeignKey(
            entity = Account::class,
            parentColumns = ["accountId"],
            childColumns = ["sourceAccount"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Contact(
    @PrimaryKey @ColumnInfo(name = "contact_id") val contactId: String, // contact account_id
    @ColumnInfo(name = "first_name") val firstName: String?,
    @ColumnInfo(name = "last_name") val lastName: String?,
    // FK: ref na account
    @ColumnInfo(name = "source_account") val sourceAccount: String?
    // ...
)