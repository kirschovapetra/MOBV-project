package sk.stuba.fei.uim.mobv_project.data.entities

import androidx.room.*

@Entity(
    tableName = "account",
    indices = [
        Index(value = ["account_id"], unique = true)
    ]
)
data class Account(
    @PrimaryKey @ColumnInfo(name = "account_id") var accountId: String = "",
    @ColumnInfo(name = "first_name") var firstName: String? = "",
    @ColumnInfo(name = "last_name") var lastName: String? = "",
//    @ColumnInfo(name = "pin") var pin: String?,
    @ColumnInfo(name = "private_key") var privateKey: String? = "",
) : AppDbEntity()
