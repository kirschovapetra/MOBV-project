package sk.stuba.fei.uim.mobv_project.data.entities

import androidx.room.*

@Entity(tableName = "account")
data class Account(
    @PrimaryKey @ColumnInfo(name = "account_id") val accountId: String,
    @ColumnInfo(name = "first_name") val firstName: String?,
    @ColumnInfo(name = "last_name") val lastName: String?,
    @ColumnInfo(name = "pin") val pin: String?,
    @ColumnInfo(name = "private_key") val privateKey: String?,
    @ColumnInfo(name = "sequence") val sequence: String?,
    // + dalsie veci ak by bolo treba
    ) : AppDbEntity()

