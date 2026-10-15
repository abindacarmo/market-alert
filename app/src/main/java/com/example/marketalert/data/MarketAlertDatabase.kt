package com.example.marketalert.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Store::class, ShoppingItem::class], version = 1, exportSchema = false)
abstract class MarketAlertDatabase : RoomDatabase() {
    abstract fun storeDao(): StoreDao
    abstract fun shoppingItemDao(): ShoppingItemDao

    companion object {
        @Volatile
        private var INSTANCE: MarketAlertDatabase? = null

        fun getInstance(context: Context): MarketAlertDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MarketAlertDatabase::class.java,
                    "market_alert.db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}