package sk.stuba.fei.uim.mobv_project.data

import android.content.Context
import androidx.room.Room.databaseBuilder
import androidx.room.*
import sk.stuba.fei.uim.mobv_project.data.dao.*
import sk.stuba.fei.uim.mobv_project.data.entities.*

import androidx.room.RoomDatabase

@Database(
    entities = [Account::class, Balances::class, Contact::class, Payment::class],
    version = 1,
    exportSchema = false // true
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract val accountDao: AccountDao
    abstract val balanceDao: BalanceDao
    abstract val contactDao: ContactDao
    abstract val paymentDao: PaymentDao

    companion object {
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
//                        .allowMainThreadQueries() // toto iba na testing
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}
