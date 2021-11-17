package sk.stuba.fei.uim.mobv_project.modules.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import sk.stuba.fei.uim.mobv_project.modules.database.dao.AccountDao
import sk.stuba.fei.uim.mobv_project.modules.database.dao.BalanceHistoryDao
import sk.stuba.fei.uim.mobv_project.modules.database.dao.ContactDao
import sk.stuba.fei.uim.mobv_project.modules.database.dao.PaymentDao
import sk.stuba.fei.uim.mobv_project.modules.database.entities.Account
import sk.stuba.fei.uim.mobv_project.modules.database.entities.BalanceHistory
import sk.stuba.fei.uim.mobv_project.modules.database.entities.Contact
import sk.stuba.fei.uim.mobv_project.modules.database.entities.Payment

@Database(
    entities = [Account::class, BalanceHistory::class, Contact::class, Payment::class],
    version = 1,
    exportSchema = false
)
//@TypeConverters(Converters::class) // mozno sa zide, zatial nic take nemame
abstract class AppDatabase : RoomDatabase() {
    abstract val contactDao: ContactDao
    abstract val accountDao: AccountDao
    abstract val paymentDao: PaymentDao
    abstract val balanceHistoryDao: BalanceHistoryDao

    companion object {

        // For Singleton instantiation
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "app_db"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}
