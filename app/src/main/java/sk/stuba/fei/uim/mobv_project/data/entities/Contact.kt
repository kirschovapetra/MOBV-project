package sk.stuba.fei.uim.mobv_project.data.entities

import androidx.room.*

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
    @ColumnInfo(name = "first_name") val firstName: String?,
    @ColumnInfo(name = "last_name") val lastName: String?,
    // FK: ref na account
    @ColumnInfo(name = "source_account") val sourceAccount: String?
    // ...
)