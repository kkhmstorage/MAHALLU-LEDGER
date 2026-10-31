package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.model.CashTransaction
import com.example.data.model.CommunityProfile
import com.example.data.model.Household
import com.example.data.model.MahalCommunity
import com.example.data.model.SubscriptionPayment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import com.example.data.model.AdditionalCollection
import com.example.data.model.AdditionalCollectionPayment
import kotlinx.coroutines.launch

@Database(
    entities = [
        Household::class,
        SubscriptionPayment::class,
        CashTransaction::class,
        CommunityProfile::class,
        MahalCommunity::class,
        AdditionalCollection::class,
        AdditionalCollectionPayment::class
    ],
    version = 5,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun householdDao(): HouseholdDao
    abstract fun subscriptionPaymentDao(): SubscriptionPaymentDao
    abstract fun cashTransactionDao(): CashTransactionDao
    abstract fun communityProfileDao(): CommunityProfileDao
    abstract fun mahalCommunityDao(): MahalCommunityDao
    abstract fun additionalCollectionDao(): AdditionalCollectionDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "mahal_ledger_database"
                )
                .fallbackToDestructiveMigration()
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        INSTANCE?.let { database ->
                            scope.launch(Dispatchers.IO) {
                                DatabaseInitializer.seedDatabase(database)
                            }
                        }
                    }
                })
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
